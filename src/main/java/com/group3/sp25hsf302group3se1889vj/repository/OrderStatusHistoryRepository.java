package com.group3.sp25hsf302group3se1889vj.repository;

import com.group3.sp25hsf302group3se1889vj.entity.OrderStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderStatusHistoryRepository extends JpaRepository<OrderStatusHistory, Long> {
}
