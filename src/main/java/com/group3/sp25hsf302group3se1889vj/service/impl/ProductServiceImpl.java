package com.group3.sp25hsf302group3se1889vj.service.impl;

import com.group3.sp25hsf302group3se1889vj.dto.ProductDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.ProductFilterDTO;
import com.group3.sp25hsf302group3se1889vj.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    @Override
    public Page<ProductDTO> findAll(ProductFilterDTO filterDTO, Pageable pageable) {
        return null;
    }

    @Override
    public void save(ProductDTO product) {

    }

    @Override
    public ProductDTO findById(Long id) {
        return null;
    }

    @Override
    public void update(ProductDTO product) {

    }
}
