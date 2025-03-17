package com.group3.sp25hsf302group3se1889vj.mapper;

import com.group3.sp25hsf302group3se1889vj.dto.OrderDTO;
import com.group3.sp25hsf302group3se1889vj.entity.Order;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final ModelMapper modelMapper;

    public OrderDTO toDTO(Order entity) {
        return modelMapper.map(entity, OrderDTO.class);
    }

    public Order toEntity(OrderDTO dto) {
        return modelMapper.map(dto, Order.class);
    }
}
