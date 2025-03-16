package com.group3.sp25hsf302group3se1889vj.mapper;

import com.group3.sp25hsf302group3se1889vj.dto.ProductVariantDTO;
import com.group3.sp25hsf302group3se1889vj.entity.ProductVariant;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductVariantMapper {

    private final ModelMapper modelMapper;

    public ProductVariantDTO toDTO(ProductVariant entity) {
        return modelMapper.map(entity, ProductVariantDTO.class);
    }

    public ProductVariant toEntity(ProductVariantDTO dto) {
        return modelMapper.map(dto, ProductVariant.class);
    }
}
