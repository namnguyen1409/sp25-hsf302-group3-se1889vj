package com.group3.sp25hsf302group3se1889vj.mapper;

import com.group3.sp25hsf302group3se1889vj.dto.BrandDTO;
import com.group3.sp25hsf302group3se1889vj.entity.Brand;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BrandMapper {

    private final ModelMapper modelMapper;

    public BrandDTO toDTO(Brand entity) {
        return modelMapper.map(entity, BrandDTO.class);
    }

    public Brand toEntity(BrandDTO dto) {
        return modelMapper.map(dto, Brand.class);
    }
}
