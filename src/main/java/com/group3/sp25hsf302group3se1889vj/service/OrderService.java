package com.group3.sp25hsf302group3se1889vj.service;

import com.group3.sp25hsf302group3se1889vj.dto.OrderDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.OrderFilterDTO;
import com.group3.sp25hsf302group3se1889vj.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    Page<OrderDTO> findAll(OrderFilterDTO filterDTO, Pageable pageable);


    Long order(OrderDTO orderDTO);

    OrderDTO findById(Long orderId);

    void updateOrderStatus(Long orderId, OrderStatus orderStatus);
}