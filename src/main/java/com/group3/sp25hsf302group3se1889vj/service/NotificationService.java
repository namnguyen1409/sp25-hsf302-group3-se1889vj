package com.group3.sp25hsf302group3se1889vj.service;

import com.group3.sp25hsf302group3se1889vj.dto.BannerDTO;
import com.group3.sp25hsf302group3se1889vj.dto.NotificationDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.NotificationFilterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationService {
    Page<NotificationDTO> findAll(NotificationFilterDTO filterDTO, Pageable pageable);

    void save(NotificationDTO notificationDTO);

    BannerDTO findById(Long id);

    void update(NotificationDTO notificationDTO);

    void delete(Long id);

}