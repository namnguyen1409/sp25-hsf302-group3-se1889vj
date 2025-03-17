package com.group3.sp25hsf302group3se1889vj.repository;

import com.group3.sp25hsf302group3se1889vj.dto.ShoppingCartDTO;
import com.group3.sp25hsf302group3se1889vj.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    Optional<ShoppingCart> findByCreatedByAndProductIdAndProductVariantId(String createdBy, Long productId, Long productVariantId);


    Optional<ShoppingCart> findByCreatedByAndProductIdAndProductVariantIdIsNull(String createdBy, Long productId);


    @Query("SELECT SUM(quantity) FROM ShoppingCart WHERE createdBy = ?1")
    int countTotalQuantityByCreatedBy(String username);

    List<ShoppingCart> findAllByCreatedBy(String currentUsername);

    boolean existsByCreatedByAndId(String currentUsername, Long id);

    void deleteAllByCreatedBy(String createdBy);
}
