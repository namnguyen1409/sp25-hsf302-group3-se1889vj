package com.group3.sp25hsf302group3se1889vj.mapper;

import com.group3.sp25hsf302group3se1889vj.dto.OrderTransactionDTO;
import com.group3.sp25hsf302group3se1889vj.entity.OrderTransaction;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderTransactionMapper {

    private final ModelMapper modelMapper;

    public OrderTransactionDTO toDTO(OrderTransaction entity) {
        return modelMapper.map(entity, OrderTransactionDTO.class);
    }

    public OrderTransaction toEntity(OrderTransactionDTO dto) {
        return modelMapper.map(dto, OrderTransaction.class);
    }
}
