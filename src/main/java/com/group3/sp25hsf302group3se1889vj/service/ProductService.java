package com.group3.sp25hsf302group3se1889vj.service;

import com.group3.sp25hsf302group3se1889vj.dto.ProductDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.ProductFilterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    Page<ProductDTO> findAll(ProductFilterDTO filterDTO, Pageable pageable);

    void save(ProductDTO product);

    ProductDTO findById(Long id);

    void update(ProductDTO product);

    boolean existsByCategoryId(Long id);
}