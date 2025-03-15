package com.group3.sp25hsf302group3se1889vj.mapper;

import com.group3.sp25hsf302group3se1889vj.dto.BannerDTO;
import com.group3.sp25hsf302group3se1889vj.entity.Banner;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BannerMapper {

    private final ModelMapper modelMapper;

    public BannerDTO toDTO(Banner entity) {
        return modelMapper.map(entity, BannerDTO.class);
    }

    public Banner toEntity(BannerDTO dto) {
        return modelMapper.map(dto, Banner.class);
    }
}
