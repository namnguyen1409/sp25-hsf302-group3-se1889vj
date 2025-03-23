package com.group3.sp25hsf302group3se1889vj.service.impl;

import com.group3.sp25hsf302group3se1889vj.dto.*;
import com.group3.sp25hsf302group3se1889vj.dto.filter.OrderFilterDTO;
import com.group3.sp25hsf302group3se1889vj.entity.*;
import com.group3.sp25hsf302group3se1889vj.enums.*;
import com.group3.sp25hsf302group3se1889vj.mapper.OrderMapper;
import com.group3.sp25hsf302group3se1889vj.repository.*;
import com.group3.sp25hsf302group3se1889vj.service.*;
import com.group3.sp25hsf302group3se1889vj.specification.OrderSpecification;
import com.group3.sp25hsf302group3se1889vj.util.SecurityUtil;
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
    private final NotificationService notificationService;
    private final OrderTransactionRepository orderTransactionRepository;

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

        OrderStatusHistory orderStatusHistory = new OrderStatusHistory();
        orderStatusHistory.setOrder(order);
        orderStatusHistory.setOrderStatus(OrderStatus.PENDING_PAYMENT);
        orderStatusHistoryRepository.save(orderStatusHistory);

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
            if (productVariant != null) {
                productVariantRepository.subtractQuantity(productVariant.getId(), item.getQuantity());
            } else {
                productRepository.subtractQuantity(product.getId(), item.getQuantity());
            }

            OrderItem orderItem = createOrderItem(order, product, productVariant, item.getQuantity());
            totalPrice = totalPrice.add(orderItem.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        BigDecimal discount = applyCoupon(orderDTO, totalPrice);

        order.setTotalPrice(totalPrice);
        order.setDiscountAmount(discount);
        order.setFinalPrice(totalPrice.subtract(discount));
        orderRepository.save(order);

        shoppingCartService.deleteAllCartByCreatedBy(orderDTO.getCreatedBy());
        return order.getId();
    }

    @Override
    public OrderDTO findById(Long orderId) {
        return orderRepository.findById(orderId).map(orderMapper::toDTO).orElse(null);
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
        sendOrderNotification("thanh toán", order, NotificationType.INFO);
        sendOrderNotificationToCustomer("thanh toán", order, NotificationType.INFO);
    }

    @Transactional
    @Override
    public void cancelOrder(Long id, String reason) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng"));

        if (order.getStatus() != OrderStatus.PENDING_PAYMENT && order.getStatus() != OrderStatus.PAYMENT_SUCCESS && order.getStatus() != OrderStatus.CONFIRMED) {
            throw new IllegalArgumentException("Không thể hủy đơn hàng");
        }
        if (reason == null || reason.isEmpty()) {
            throw new IllegalArgumentException("Lý do hủy không được để trống");
        }

        // hoàn tiền nếu đã thanh toán
        if (order.getStatus() == OrderStatus.PAYMENT_SUCCESS || order.getStatus() == OrderStatus.CONFIRMED) {
            // hoàn tiền
            OrderTransaction orderTransaction = new OrderTransaction();
            orderTransaction.setOrder(order);
            orderTransaction.setAmount(order.getFinalPrice());
            orderTransaction.setStatus(OrderTransactionStatus.SUCCESS);
            orderTransaction.setVnPayResponseCode("00");

            orderTransaction.setType(OrderTransactionType.REFUND);
            orderTransaction.setCreatedBy(SecurityUtil.getCurrentUsername());
            orderTransactionRepository.save(orderTransaction);
            sendOrderNotification("hoàn tiền", order, NotificationType.WARNING);
            sendOrderNotificationToCustomer("hoàn tiền", order, NotificationType.WARNING);

            order.setStatus(OrderStatus.REFUNDED);
            orderRepository.save(order);

            OrderStatusHistory orderStatusHistory1 = new OrderStatusHistory();
            orderStatusHistory1.setOrder(order);
            orderStatusHistory1.setOrderStatus(OrderStatus.REFUNDED);
            orderStatusHistoryRepository.save(orderStatusHistory1);
        }

        order.setStatus(OrderStatus.CANCELLED);
        order.setCancelReason(reason);
        orderRepository.save(order);

        OrderStatusHistory orderStatusHistory = new OrderStatusHistory();
        orderStatusHistory.setOrder(order);
        orderStatusHistory.setOrderStatus(OrderStatus.CANCELLED);
        orderStatusHistoryRepository.save(orderStatusHistory);

        sendOrderNotification("hủy", order, NotificationType.WARNING);
        sendOrderNotificationToCustomer("hủy", order, NotificationType.WARNING);

        // Trả lại số lượng sản phẩm
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(id);
        for (OrderItem orderItem : orderItems) {
            if (orderItem.getProductVariant() != null) {
                productVariantRepository.addQuantity(orderItem.getProductVariant().getId(), orderItem.getQuantity());
            } else {
                productRepository.addQuantity(orderItem.getProduct().getId(), orderItem.getQuantity());
            }
        }

    }

    @Override
    public void confirmOrder(Long id, OrderStatus orderStatus) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng"));

        if (order.getStatus() != OrderStatus.PAYMENT_SUCCESS) {
            throw new IllegalArgumentException("Không thể xác nhận đơn hàng");
        }

        order.setStatus(orderStatus);
        orderRepository.save(order);

        OrderStatusHistory orderStatusHistory = new OrderStatusHistory();
        orderStatusHistory.setOrder(order);
        orderStatusHistory.setOrderStatus(orderStatus);
        orderStatusHistoryRepository.save(orderStatusHistory);

        sendOrderNotification("xác nhận", order, NotificationType.INFO);
        sendOrderNotificationToCustomer("xác nhận", order, NotificationType.INFO);

    }

    @Override
    public void shipOrder(Long id, OrderStatus orderStatus) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng"));

        if (order.getStatus() != OrderStatus.CONFIRMED) {
            throw new IllegalArgumentException("Không thể giao hàng");
        }

        order.setStatus(orderStatus);
        orderRepository.save(order);

        OrderStatusHistory orderStatusHistory = new OrderStatusHistory();
        orderStatusHistory.setOrder(order);
        orderStatusHistory.setOrderStatus(orderStatus);
        orderStatusHistoryRepository.save(orderStatusHistory);

        sendOrderNotification("trên đường vận chuyển", order, NotificationType.INFO);
        sendOrderNotificationToCustomer("trên đường vận chuyển", order, NotificationType.INFO);
    }

    @Override
    public void deliverOrder(Long id, OrderStatus orderStatus) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng"));

        if (order.getStatus() != OrderStatus.SHIPPED) {
            throw new IllegalArgumentException("Không thể giao hàng");
        }

        order.setStatus(orderStatus);
        orderRepository.save(order);

        OrderStatusHistory orderStatusHistory = new OrderStatusHistory();
        orderStatusHistory.setOrder(order);
        orderStatusHistory.setOrderStatus(orderStatus);
        orderStatusHistoryRepository.save(orderStatusHistory);

        sendOrderNotification("giao hàng", order, NotificationType.INFO);
        sendOrderNotificationToCustomer("giao hàng", order, NotificationType.INFO);
    }

    @Override
    public boolean isExistByAddressId(Long id) {
        return orderRepository.existsByCustomerAddressId(id);
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

        return discount.min(totalPrice);
    }

    private void validateStock(int orderQuantity, int productStock) {
        if (orderQuantity > productStock) {
            throw new IllegalArgumentException("Số lượng sản phẩm không đủ");
        }
    }

    private void sendOrderNotification(String action, Order order, NotificationType type) {
        var username = SecurityUtil.getCurrentUsername();
        NotificationDTO notification = new NotificationDTO();
        notification.setType(type);
        notification.setTitle("Đơn hàng đã " + action);
        notification.setContent("Đơn hàng \"" + order.getId() + "\" đã " + action + " bởi " + username);
        notificationService.sendNotificationToPermission(notification, PermissionType.MANAGE_ORDERS);
    }

    private void sendOrderNotificationToCustomer(String action, Order order, NotificationType type) {
        NotificationDTO notification = new NotificationDTO();
        notification.setType(type);
        notification.setTitle("Đơn hàng đã " + action);
        notification.setContent("Đơn hàng \"" + order.getId() + "\" đã " + action);
        notificationService.sendNotificationToUser(notification, order.getCreatedBy());
    }

}
