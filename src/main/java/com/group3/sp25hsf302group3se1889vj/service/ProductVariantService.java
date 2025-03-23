package com.group3.sp25hsf302group3se1889vj.service;

import com.group3.sp25hsf302group3se1889vj.dto.ProductVariantDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.ProductVariantFilterDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductVariantService extends PagingService<ProductVariantDTO, ProductVariantFilterDTO> {

    Page<ProductVariantDTO> findAll(ProductVariantFilterDTO filterDTO, Pageable pageable);

    void save(ProductVariantDTO productVariantDTO);

    void update(ProductVariantDTO productVariantDTO);

    boolean existsByProductIdAndSizeAndColor(Long productId, String size, String color);

    ProductVariantDTO findById(Long id);

    boolean existsByProductIdAndSizeAndColorAndIdNot(Long productId, String size, String color, Long id);
}