package com.group3.sp25hsf302group3se1889vj.service.impl;

import com.group3.sp25hsf302group3se1889vj.dto.ProductVariantDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.ProductVariantFilterDTO;
import com.group3.sp25hsf302group3se1889vj.entity.Product;
import com.group3.sp25hsf302group3se1889vj.entity.ProductVariant;
import com.group3.sp25hsf302group3se1889vj.mapper.ProductVariantMapper;
import com.group3.sp25hsf302group3se1889vj.repository.ProductRepository;
import com.group3.sp25hsf302group3se1889vj.repository.ProductVariantRepository;
import com.group3.sp25hsf302group3se1889vj.service.ProductVariantService;
import com.group3.sp25hsf302group3se1889vj.specification.ProductVariantSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductVariantServiceImpl implements ProductVariantService {
    private final ProductVariantRepository productVariantRepository;
    private final ProductVariantMapper productVariantMapper;
    private final ProductRepository productRepository;

    @Override
    public Page<ProductVariantDTO> findAll(ProductVariantFilterDTO filterDTO, Pageable pageable) {
        Specification<ProductVariant> specification = ProductVariantSpecification.filterProductVariant(filterDTO);
        return productVariantRepository.findAll(specification, pageable)
                .map(productVariantMapper::toDTO);
    }

    @Override
    public void save(ProductVariantDTO productVariantDTO) {
        ProductVariant productVariant = new ProductVariant();
        productVariant.setSize(productVariantDTO.getSize());
        productVariant.setColor(productVariantDTO.getColor());
        productVariant.setQuantity(productVariantDTO.getQuantity());
        Product product = productRepository.findById(productVariantDTO.getProductId()).orElseThrow();
        productVariant.setProduct(product);
        productVariantRepository.save(productVariant);
    }

    @Override
    public void update(ProductVariantDTO productVariantDTO) {
        ProductVariant productVariant = productVariantRepository.findById(productVariantDTO.getId()).orElseThrow();
        productVariant.setSize(productVariantDTO.getSize());
        productVariant.setColor(productVariantDTO.getColor());
        productVariant.setQuantity(productVariantDTO.getQuantity());
        productVariantRepository.save(productVariant);
    }

    @Override
    public boolean existsByProductIdAndSizeAndColor(Long productId, String size, String color) {
        return productVariantRepository.existsByProductIdAndSizeAndColor(productId, size, color);
    }

    @Override
    public ProductVariantDTO findById(Long id) {
        return productVariantRepository.findById(id).map(productVariantMapper::toDTO).orElseThrow();
    }

    @Override
    public boolean existsByProductIdAndSizeAndColorAndIdNot(Long productId, String size, String color, Long id) {
        return productVariantRepository.existsByProductIdAndSizeAndColorAndIdNot(productId, size, color, id);
    }
}
