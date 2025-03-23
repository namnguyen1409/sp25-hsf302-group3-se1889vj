package com.group3.sp25hsf302group3se1889vj.service;

import com.group3.sp25hsf302group3se1889vj.dto.OrderTransactionDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.OrderTransactionFilterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderTransactionService extends PagingService<OrderTransactionDTO, OrderTransactionFilterDTO> {
    void save(OrderTransactionDTO orderTransactionDTO);

    OrderTransactionDTO findById(Long id);

    void update(OrderTransactionDTO orderTransaction);

    OrderTransactionDTO getOrderTransactionById(Long id);
}
