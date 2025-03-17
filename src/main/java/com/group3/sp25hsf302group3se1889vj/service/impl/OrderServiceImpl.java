package com.group3.sp25hsf302group3se1889vj.service.impl;

import com.group3.sp25hsf302group3se1889vj.dto.OrderDTO;
import com.group3.sp25hsf302group3se1889vj.dto.OrderItemDTO;
import com.group3.sp25hsf302group3se1889vj.dto.ShoppingCartDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.OrderFilterDTO;
import com.group3.sp25hsf302group3se1889vj.entity.*;
import com.group3.sp25hsf302group3se1889vj.enums.CouponType;
import com.group3.sp25hsf302group3se1889vj.enums.OrderStatus;
import com.group3.sp25hsf302group3se1889vj.mapper.OrderMapper;
import com.group3.sp25hsf302group3se1889vj.repository.*;
import com.group3.sp25hsf302group3se1889vj.service.OrderItemService;
import com.group3.sp25hsf302group3se1889vj.service.OrderService;
import com.group3.sp25hsf302group3se1889vj.service.ShoppingCartService;
import com.group3.sp25hsf302group3se1889vj.service.UserCouponService;
import com.group3.sp25hsf302group3se1889vj.specification.OrderSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ProductRepository productRepository;
    private final ProductVariantRepository productVariantRepository;
    private final CustomerAddressRepository customerAddressRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CouponRepository couponRepository;
    private final UserCouponService userCouponService;
    private final OrderItemService orderItemService;
    private final ShoppingCartService shoppingCartService;
    private final ModelMapper modelMapper;
    private final OrderStatusHistoryRepository orderStatusHistoryRepository;
    private final OrderMapper orderMapper;

    @Override
    public Page<OrderDTO> findAll(OrderFilterDTO filterDTO, Pageable pageable) {

        Specification<Order> specification = OrderSpecification.filterOrder(filterDTO);
        return orderRepository.findAll(specification, pageable)
                .map(orderMapper::toDTO);
    }


    @Transactional
    @Override
    public Long order(OrderDTO orderDTO) {
        CustomerAddress address = customerAddressRepository.findById(orderDTO.getAddressId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy địa chỉ"));

        Order order = new Order();
        order.setCreatedBy(orderDTO.getCreatedBy());
        order.setCustomerAddress(address);
        order.setNote(orderDTO.getNote());
        order.setStatus(OrderStatus.PENDING_PAYMENT);
        order = orderRepository.save(order);

        BigDecimal totalPrice = BigDecimal.ZERO;
        List<ShoppingCartDTO> shoppingCart = shoppingCartService.getAllCartByCreatedBy(orderDTO.getCreatedBy());

        for (var item : shoppingCart) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm"));

            ProductVariant productVariant = null;
            int availableStock;

            if (!product.getVariants().isEmpty() && item.getProductVariantId() == null) {
                throw new IllegalArgumentException("Phải chọn biến thể");
            }

            if (item.getProductVariantId() != null) {
                productVariant = product.getVariants().stream()
                        .filter(v -> v.getId().equals(item.getProductVariantId()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy biến thể sản phẩm"));

                availableStock = productVariant.getQuantity();
            } else {
                availableStock = product.getQuantity();
            }

            validateStock(item.getQuantity(), availableStock);

            // Trừ số lượng ngay lập tức
            if (productVariant != null) {
                productVariantRepository.subtractQuantity(productVariant.getId(), item.getQuantity());
            } else {
                productRepository.subtractQuantity(product.getId(), item.getQuantity());
            }

            OrderItem orderItem = createOrderItem(order, product, productVariant, item.getQuantity());
            totalPrice = totalPrice.add(orderItem.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        // Xử lý mã giảm giá
        BigDecimal discount = applyCoupon(orderDTO, totalPrice);

        order.setDiscountAmount(discount);
        order.setFinalPrice(totalPrice.subtract(discount));
        orderRepository.save(order);

        shoppingCartService.deleteAllCartByCreatedBy(orderDTO.getCreatedBy());
        return order.getId();
    }

    @Override
    public OrderDTO findById(Long orderId) {
        return orderRepository.findById(orderId).map((element) -> modelMapper.map(element, OrderDTO.class)).orElse(null);
    }

    @Override
    public void updateOrderStatus(Long orderId, OrderStatus orderStatus) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng"));
        order.setStatus(orderStatus);
        orderRepository.save(order);

        OrderStatusHistory orderStatusHistory = new OrderStatusHistory();
        orderStatusHistory.setOrder(order);
        orderStatusHistory.setOrderStatus(orderStatus);
        orderStatusHistoryRepository.save(orderStatusHistory);

    }

    private OrderItem createOrderItem(Order order, Product product, ProductVariant variant, int quantity) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setProductVariant(variant);
        orderItem.setQuantity(quantity);
        orderItem.setPrice(product.getPriceSale());
        return orderItemRepository.save(orderItem);
    }

    private BigDecimal applyCoupon(OrderDTO orderDTO, BigDecimal totalPrice) {
        if (orderDTO.getCouponCode() == null || orderDTO.getCouponCode().isEmpty()) {
            return BigDecimal.ZERO;
        }

        Coupon coupon = couponRepository.findByCode(orderDTO.getCouponCode());
        if (coupon == null) {
            throw new IllegalArgumentException("Mã giảm giá không tồn tại");
        }
        if (!coupon.isValid(totalPrice)) {
            throw new IllegalArgumentException("Mã giảm giá không hợp lệ");
        }

        int count = userCouponService.countByCouponCodeAndCreatedBy(coupon.getCode(), orderDTO.getCreatedBy());
        if (count >= coupon.getMaxUsagePerUser()) {
            throw new IllegalArgumentException("Mã giảm giá đã hết lượt sử dụng");
        }

        userCouponService.useCoupon(coupon.getCode(), orderDTO.getCreatedBy());

        BigDecimal discount = (coupon.getType() == CouponType.PERCENTAGE)
                ? totalPrice.multiply(coupon.getValue()).divide(BigDecimal.valueOf(100))
                : coupon.getValue();

        if (coupon.getMaxDiscount() != null && discount.compareTo(coupon.getMaxDiscount()) > 0) {
            discount = coupon.getMaxDiscount();
        }

        return discount.min(totalPrice); // Không giảm quá tổng tiền
    }

    private void validateStock(int orderQuantity, int productStock) {
        if (orderQuantity > productStock) {
            throw new IllegalArgumentException("Số lượng sản phẩm không đủ");
        }
    }

}
