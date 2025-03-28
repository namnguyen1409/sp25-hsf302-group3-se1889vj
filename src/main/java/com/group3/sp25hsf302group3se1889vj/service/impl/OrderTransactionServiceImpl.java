package com.group3.sp25hsf302group3se1889vj.service.impl;

import com.group3.sp25hsf302group3se1889vj.dto.NotificationDTO;
import com.group3.sp25hsf302group3se1889vj.dto.OrderTransactionDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.OrderTransactionFilterDTO;
import com.group3.sp25hsf302group3se1889vj.entity.OrderTransaction;
import com.group3.sp25hsf302group3se1889vj.enums.NotificationType;
import com.group3.sp25hsf302group3se1889vj.enums.OrderTransactionStatus;
import com.group3.sp25hsf302group3se1889vj.mapper.OrderTransactionMapper;
import com.group3.sp25hsf302group3se1889vj.repository.OrderRepository;
import com.group3.sp25hsf302group3se1889vj.repository.OrderTransactionRepository;
import com.group3.sp25hsf302group3se1889vj.service.NotificationService;
import com.group3.sp25hsf302group3se1889vj.service.OrderTransactionService;
import com.group3.sp25hsf302group3se1889vj.specification.OrderTransactionSpecification;
import com.group3.sp25hsf302group3se1889vj.util.SecurityUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderTransactionServiceImpl implements OrderTransactionService {
    private final OrderRepository orderRepository;
    private final OrderTransactionRepository orderTransactionRepository;
    private final NotificationService notificationService;
    private final OrderTransactionMapper orderTransactionMapper;

    @Override
    public void save(OrderTransactionDTO orderTransactionDTO) {
        OrderTransaction orderTransaction = new OrderTransaction();
        orderTransaction.setOrder(orderRepository.findById(Long.parseLong(orderTransactionDTO.getTransactionId())).orElseThrow());
        orderTransaction.setTransactionId(orderTransactionDTO.getTransactionId());
        orderTransaction.setAmount(orderTransactionDTO.getAmount());
        orderTransaction.setType(orderTransactionDTO.getType());
        orderTransaction.setVnPayResponseCode(orderTransactionDTO.getVnPayResponseCode());
        orderTransaction.setStatus(orderTransactionDTO.getStatus());
        orderTransactionRepository.save(orderTransaction);
        sendNotificationToUser(orderTransactionDTO, SecurityUtil.getCurrentUsername());
    }

    @Override
    public Page<OrderTransactionDTO> findAll(OrderTransactionFilterDTO filterDTO, Pageable pageable) {
        Specification<OrderTransaction> specification = OrderTransactionSpecification.filterOrderTransaction(filterDTO);
        return orderTransactionRepository.findAll(specification, pageable).map(orderTransactionMapper::toDTO);
    }

    @Override
    public OrderTransactionDTO findById(Long id) {
        return orderTransactionRepository.findById(id).map(orderTransactionMapper::toDTO).orElse(null);
    }

    @Override
    public void update(OrderTransactionDTO orderTransaction) {

    }

    @Override
    public OrderTransactionDTO getOrderTransactionById(Long id) {
        return orderTransactionRepository.findById(id).map(orderTransactionMapper::toDTO).orElse(null);
    }

    // hàm gửi thông báo cho người dùng
    public void sendNotificationToUser(OrderTransactionDTO orderTransactionDTO, String username) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setTitle("Thông báo thanh toán");
        if (orderTransactionDTO.getStatus() == OrderTransactionStatus.SUCCESS) {
            notificationDTO.setContent("Thanh toán đơn hàng " + orderTransactionDTO.getTransactionId() + " thành công");
            notificationDTO.setType(NotificationType.SUCCESS);
        } else {
            notificationDTO.setContent("Thanh toán đơn hàng " + orderTransactionDTO.getTransactionId() + " thất bại");
            notificationDTO.setType(NotificationType.ERROR);
        }
        notificationService.sendNotificationToUser(notificationDTO, username);
    }

}
