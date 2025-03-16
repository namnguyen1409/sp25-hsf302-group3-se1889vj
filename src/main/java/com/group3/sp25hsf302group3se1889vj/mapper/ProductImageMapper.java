package com.group3.sp25hsf302group3se1889vj.mapper;

import com.group3.sp25hsf302group3se1889vj.dto.ProductImageDTO;
import com.group3.sp25hsf302group3se1889vj.entity.ProductImage;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductImageMapper {

    private final ModelMapper modelMapper;

    public ProductImageDTO toDTO(ProductImage entity) {
        return modelMapper.map(entity, ProductImageDTO.class);
    }

    public ProductImage toEntity(ProductImageDTO dto) {
        return modelMapper.map(dto, ProductImage.class);
    }
}
