package com.group3.sp25hsf302group3se1889vj.service.impl;

import com.group3.sp25hsf302group3se1889vj.dto.BannerDTO;
import com.group3.sp25hsf302group3se1889vj.dto.NotificationDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.NotificationFilterDTO;
import com.group3.sp25hsf302group3se1889vj.service.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    @Override
    public Page<NotificationDTO> findAll(NotificationFilterDTO filterDTO, Pageable pageable) {
        return null;
    }

    @Override
    public void save(NotificationDTO notificationDTO) {

    }

    @Override
    public BannerDTO findById(Long id) {
        return null;
    }

    @Override
    public void update(NotificationDTO notificationDTO) {

    }

    @Override
    public void delete(Long id) {

    }
}
