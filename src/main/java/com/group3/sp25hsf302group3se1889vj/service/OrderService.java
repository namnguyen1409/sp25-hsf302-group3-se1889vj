package com.group3.sp25hsf302group3se1889vj.service;

import com.group3.sp25hsf302group3se1889vj.dto.OrderDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.OrderFilterDTO;
import com.group3.sp25hsf302group3se1889vj.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService extends PagingService<OrderDTO, OrderFilterDTO>{

    Long order(OrderDTO orderDTO);

    OrderDTO findById(Long orderId);

    void updateOrderStatus(Long orderId, OrderStatus orderStatus);

    void cancelOrder(Long id, String reason);

    void confirmOrder(Long id, OrderStatus orderStatus);

    void shipOrder(Long id, OrderStatus orderStatus);

    void deliverOrder(Long id, OrderStatus orderStatus);

    boolean isExistByAddressId(Long id);
}