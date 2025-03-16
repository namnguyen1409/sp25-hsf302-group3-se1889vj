package com.group3.sp25hsf302group3se1889vj.service.impl;

import com.group3.sp25hsf302group3se1889vj.dto.ProductImageDTO;
import com.group3.sp25hsf302group3se1889vj.entity.ProductImage;
import com.group3.sp25hsf302group3se1889vj.mapper.ProductImageMapper;
import com.group3.sp25hsf302group3se1889vj.repository.ProductImageRepository;
import com.group3.sp25hsf302group3se1889vj.repository.ProductRepository;
import com.group3.sp25hsf302group3se1889vj.service.ProductImageService;
import jakarta.persistence.Table;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductImageServiceImpl implements ProductImageService {
    private final ProductImageRepository productImageRepository;
    private final ProductImageMapper productImageMapper;
    private final ProductRepository productRepository;

    @Override
    public List<ProductImageDTO> findAllByProductId(Long id) {
        return productImageRepository.findAllByProductId(id).stream().map(productImageMapper::toDTO).toList();
    }

    @Override
    public void updateImageOrder(List<Long> imageIds) {
        for (int i = 0; i < imageIds.size(); i++) {
            var image = productImageRepository.findById(imageIds.get(i)).orElseThrow();
            image.setPosition(i + 1);
            productImageRepository.save(image);
        }
    }

    @Override
    public void deleteImage(Long imageId) {
        productImageRepository.deleteById(imageId);
    }

    @Override
    public void save(ProductImageDTO productImageDTO) {
        if (productImageDTO == null || productImageDTO.getProductId() == null) {
            throw new IllegalArgumentException("Dữ liệu không hợp lệ");
        }
        ProductImage productImage = new ProductImage();
        productImage.setUrl(productImageDTO.getUrl());
        productImage.setAlt(productImageDTO.getAlt());
        productImage.setPosition(productImageRepository.countByProductId(productImageDTO.getProductId()) + 1);
        productImage.setProduct(productRepository.findById(productImageDTO.getProductId()).orElseThrow());
        productImageRepository.save(productImage);
    }
}
