package com.group3.sp25hsf302group3se1889vj.mapper;

import com.group3.sp25hsf302group3se1889vj.dto.OrderStatusHistoryDTO;
import com.group3.sp25hsf302group3se1889vj.entity.OrderStatusHistory;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderStatusHistoryMapper {

    private final ModelMapper modelMapper;

    public OrderStatusHistoryDTO toDTO(OrderStatusHistory entity) {
        return modelMapper.map(entity, OrderStatusHistoryDTO.class);
    }

    public OrderStatusHistory toEntity(OrderStatusHistoryDTO dto) {
        return modelMapper.map(dto, OrderStatusHistory.class);
    }
}
