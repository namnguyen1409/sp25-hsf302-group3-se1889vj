package com.group3.sp25hsf302group3se1889vj.service;

import com.group3.sp25hsf302group3se1889vj.dto.ProductImageDTO;

import java.util.List;

public interface ProductImageService {

    List<ProductImageDTO> findAllByProductId(Long id);

    void updateImageOrder(List<Long> imageIds);

    void deleteImage(Long imageId);

    void save(ProductImageDTO productImageDTO);
}