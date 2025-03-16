package com.group3.sp25hsf302group3se1889vj.service;

import com.group3.sp25hsf302group3se1889vj.dto.BannerDTO;
import com.group3.sp25hsf302group3se1889vj.dto.NotificationDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.NotificationFilterDTO;
import com.group3.sp25hsf302group3se1889vj.enums.PermissionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationService {
    Page<NotificationDTO> findAll(NotificationFilterDTO filterDTO, Pageable pageable);

    void save(NotificationDTO notificationDTO);

    NotificationDTO findById(Long id);

    void delete(Long id);

    void sendNotificationToPermission(NotificationDTO notificationDTO, PermissionType permissionType);

    void markAsRead(Long id);

    int countUnreadNotificationsByUserId(Long id);

    NotificationDTO findByIdAndUsername(Long id, String currentUsername);

    void deleteAll(String currentUsername);

    void markAllAsRead(String currentUsername);
}