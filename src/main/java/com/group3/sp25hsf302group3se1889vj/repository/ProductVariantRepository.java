package com.group3.sp25hsf302group3se1889vj.repository;

import com.group3.sp25hsf302group3se1889vj.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long>, JpaSpecificationExecutor<ProductVariant> {
    boolean existsByProductIdAndSizeAndColor(Long productId, String size, String color);

    boolean existsByProductIdAndSizeAndColorAndIdNot(Long productId, String size, String color, Long id);

    @Modifying
    @Query(value = "UPDATE ProductVariant pv SET pv.quantity = pv.quantity - ?2, pv.updatedAt = CURRENT_TIMESTAMP WHERE pv.id = ?1")
    void subtractQuantity(Long productVariantId, int quantity);

    @Modifying
    @Query(value = "UPDATE ProductVariant pv SET pv.quantity = pv.quantity + ?2, pv.updatedAt = CURRENT_TIMESTAMP WHERE pv.id = ?1")
    void addQuantity(Long productVariantId, int quantity);
}
