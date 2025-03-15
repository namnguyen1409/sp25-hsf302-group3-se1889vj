package com.group3.sp25hsf302group3se1889vj.service.impl;

import com.group3.sp25hsf302group3se1889vj.dto.OrderDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.OrderFilterDTO;
import com.group3.sp25hsf302group3se1889vj.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    @Override
    public Page<OrderDTO> findAll(OrderFilterDTO filterDTO, Pageable pageable) {
        return null;
    }
}
