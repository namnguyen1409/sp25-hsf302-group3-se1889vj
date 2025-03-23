package com.group3.sp25hsf302group3se1889vj.service.impl;

import com.group3.sp25hsf302group3se1889vj.dto.NotificationDTO;
import com.group3.sp25hsf302group3se1889vj.dto.OrderDTO;
import com.group3.sp25hsf302group3se1889vj.dto.ShoppingCartDTO;
import com.group3.sp25hsf302group3se1889vj.entity.Order;
import com.group3.sp25hsf302group3se1889vj.entity.ShoppingCart;
import com.group3.sp25hsf302group3se1889vj.enums.NotificationType;
import com.group3.sp25hsf302group3se1889vj.mapper.ShoppingCartMapper;
import com.group3.sp25hsf302group3se1889vj.repository.ProductRepository;
import com.group3.sp25hsf302group3se1889vj.repository.ShoppingCartRepository;
import com.group3.sp25hsf302group3se1889vj.service.NotificationService;
import com.group3.sp25hsf302group3se1889vj.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductRepository productRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final NotificationService notificationService;

    @Transactional
    @Override
    public void addToCart(ShoppingCartDTO shoppingCartDTO) {
        log.info("Thêm vào giỏ hàng: {}", shoppingCartDTO);

        var product = productRepository.findById(shoppingCartDTO.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm"));

        if (!product.getVariants().isEmpty() && shoppingCartDTO.getProductVariantId() == null) {
            throw new IllegalArgumentException("Phải chọn biến thể");
        }

        if (shoppingCartDTO.getProductVariantId() != null) {
            var productVariant = product.getVariants().stream()
                    .filter(v -> v.getId().equals(shoppingCartDTO.getProductVariantId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy biến thể sản phẩm"));

            var cart = shoppingCartRepository.findByCreatedByAndProductIdAndProductVariantId(
                    shoppingCartDTO.getCreatedBy(), shoppingCartDTO.getProductId(), shoppingCartDTO.getProductVariantId()
            );

            cart.ifPresentOrElse(existingCart -> {
                validateStock(existingCart.getQuantity(), shoppingCartDTO.getQuantity(), productVariant.getQuantity());
                existingCart.setQuantity(existingCart.getQuantity() + shoppingCartDTO.getQuantity());
                shoppingCartRepository.save(existingCart);
            }, () -> {
                validateStock(0, shoppingCartDTO.getQuantity(), productVariant.getQuantity());
                ShoppingCart shoppingCart = new ShoppingCart();
                shoppingCart.setCreatedBy(shoppingCartDTO.getCreatedBy());
                shoppingCart.setProduct(product);
                shoppingCart.setProductVariant(productVariant);
                shoppingCart.setQuantity(shoppingCartDTO.getQuantity());
                shoppingCartRepository.save(shoppingCart);
            });

        } else {
            var cart = shoppingCartRepository.findByCreatedByAndProductIdAndProductVariantIdIsNull(
                    shoppingCartDTO.getCreatedBy(), shoppingCartDTO.getProductId()
            );

            cart.ifPresentOrElse(existingCart -> {
                validateStock(existingCart.getQuantity(), shoppingCartDTO.getQuantity(), product.getQuantity());
                existingCart.setQuantity(existingCart.getQuantity() + shoppingCartDTO.getQuantity());
                shoppingCartRepository.save(existingCart);
            }, () -> {
                validateStock(0, shoppingCartDTO.getQuantity(), product.getQuantity());
                ShoppingCart shoppingCart = new ShoppingCart();
                shoppingCart.setCreatedBy(shoppingCartDTO.getCreatedBy());
                shoppingCart.setProduct(product);
                shoppingCart.setQuantity(shoppingCartDTO.getQuantity());
                shoppingCartRepository.save(shoppingCart);
            });
        }

        sendNotificationToCustomer("Thêm vào giỏ hàng", "Sản phẩm đã được thêm vào giỏ hàng", shoppingCartDTO.getCreatedBy(), NotificationType.INFO);

        log.info("Thêm vào giỏ hàng thành công: {}", shoppingCartDTO);
    }

    @Override
    public int countTotalQuantityByUsername(String username) {
        return shoppingCartRepository.countTotalQuantityByCreatedBy(username);
    }

    @Override
    public List<ShoppingCartDTO> getAllCartByCreatedBy(String currentUsername) {
        return shoppingCartRepository.findAllByCreatedBy(currentUsername).stream()
                .map(shoppingCartMapper::toDTO).toList();
    }

    @Override
    public boolean isExistByCreatedByAndId(String currentUsername, Long id) {
        return shoppingCartRepository.existsByCreatedByAndId(currentUsername, id);
    }

    @Override
    public void removeFromCart(ShoppingCartDTO shoppingCartDTO) {
        shoppingCartRepository.deleteById(shoppingCartDTO.getId());
    }

    @Override
    public void updateCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(shoppingCartDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm trong giỏ hàng"));
        shoppingCart.setQuantity(shoppingCartDTO.getQuantity());
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public void deleteAllCartByCreatedBy(String createdBy) {
        shoppingCartRepository.deleteAllByCreatedBy(createdBy);
    }


    private void validateStock(int existingQuantity, int newQuantity, int stock) {
        if (existingQuantity + newQuantity > stock) {
            throw new IllegalArgumentException("Số lượng sản phẩm trong giỏ hàng vượt quá số lượng tồn kho");
        }
    }

    private void sendNotificationToCustomer(String title, String content, String to,NotificationType type) {
        NotificationDTO notification = new NotificationDTO();
        notification.setType(type);
        notification.setTitle(title);
        notification.setContent(content);
        notificationService.sendNotificationToUser(notification, to);
    }


}
