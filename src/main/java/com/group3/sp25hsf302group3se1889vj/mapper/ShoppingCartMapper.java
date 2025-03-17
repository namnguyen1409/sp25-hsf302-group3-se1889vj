package com.group3.sp25hsf302group3se1889vj.mapper;

import com.group3.sp25hsf302group3se1889vj.dto.ShoppingCartDTO;
import com.group3.sp25hsf302group3se1889vj.entity.ShoppingCart;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShoppingCartMapper {

    private final ModelMapper modelMapper;

    public ShoppingCartDTO toDTO(ShoppingCart entity) {
        return modelMapper.map(entity, ShoppingCartDTO.class);
    }

    public ShoppingCart toEntity(ShoppingCartDTO dto) {
        return modelMapper.map(dto, ShoppingCart.class);
    }
}
