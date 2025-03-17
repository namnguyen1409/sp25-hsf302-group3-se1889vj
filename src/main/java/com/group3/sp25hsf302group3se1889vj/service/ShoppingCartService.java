package com.group3.sp25hsf302group3se1889vj.service;

import com.group3.sp25hsf302group3se1889vj.dto.OrderDTO;
import com.group3.sp25hsf302group3se1889vj.dto.ShoppingCartDTO;

import java.util.List;

public interface ShoppingCartService {

    void addToCart(ShoppingCartDTO shoppingCartDTO);

    int countTotalQuantityByUsername(String username);

    List<ShoppingCartDTO> getAllCartByCreatedBy(String currentUsername);

    boolean isExistByCreatedByAndId(String currentUsername, Long id);

    void removeFromCart(ShoppingCartDTO shoppingCartDTO);

    void updateCart(ShoppingCartDTO shoppingCartDTO);

    void deleteAllCartByCreatedBy(String createdBy);
}