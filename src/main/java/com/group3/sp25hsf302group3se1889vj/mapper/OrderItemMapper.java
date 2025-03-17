package com.group3.sp25hsf302group3se1889vj.mapper;

import com.group3.sp25hsf302group3se1889vj.dto.OrderItemDTO;
import com.group3.sp25hsf302group3se1889vj.entity.OrderItem;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderItemMapper {

    private final ModelMapper modelMapper;

    public OrderItemDTO toDTO(OrderItem entity) {
        return modelMapper.map(entity, OrderItemDTO.class);
    }

    public OrderItem toEntity(OrderItemDTO dto) {
        return modelMapper.map(dto, OrderItem.class);
    }
}
