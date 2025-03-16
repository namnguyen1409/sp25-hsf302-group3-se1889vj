package com.group3.sp25hsf302group3se1889vj.repository;

import com.group3.sp25hsf302group3se1889vj.dto.ProductImageDTO;
import com.group3.sp25hsf302group3se1889vj.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findAllByProductId(Long id);

    int countByProductId(Long productId);
}
