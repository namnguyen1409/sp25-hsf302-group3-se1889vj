package com.group3.sp25hsf302group3se1889vj.mapper;

import com.group3.sp25hsf302group3se1889vj.dto.StaticPageDTO;
import com.group3.sp25hsf302group3se1889vj.entity.StaticPage;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StaticPageMapper {
    private final ModelMapper modelMapper;

    public StaticPageDTO toDTO(StaticPage entity) {
        return modelMapper.map(entity, StaticPageDTO.class);
    }

    public StaticPage toEntity(StaticPageDTO dto) {
        return modelMapper.map(dto, StaticPage.class);
    }
}
