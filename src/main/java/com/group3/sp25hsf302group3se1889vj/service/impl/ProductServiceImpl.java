package com.group3.sp25hsf302group3se1889vj.service.impl;

import com.group3.sp25hsf302group3se1889vj.dto.ProductDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.ProductFilterDTO;
import com.group3.sp25hsf302group3se1889vj.entity.Category;
import com.group3.sp25hsf302group3se1889vj.entity.Product;
import com.group3.sp25hsf302group3se1889vj.mapper.CategoryMapper;
import com.group3.sp25hsf302group3se1889vj.mapper.ProductMapper;
import com.group3.sp25hsf302group3se1889vj.repository.ProductRepository;
import com.group3.sp25hsf302group3se1889vj.service.ProductService;
import com.group3.sp25hsf302group3se1889vj.specification.CategorySpecification;
import com.group3.sp25hsf302group3se1889vj.specification.ProductSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public Page<ProductDTO> findAll(ProductFilterDTO productFilterDTO, Pageable pageable) {
        Specification<Product> specification = ProductSpecification.filterProduct(productFilterDTO);
        return productRepository.findAll(specification, pageable)
                .map(productMapper::mapToProductDTO);
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
