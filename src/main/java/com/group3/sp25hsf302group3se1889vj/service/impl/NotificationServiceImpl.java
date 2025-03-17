package com.group3.sp25hsf302group3se1889vj.service.impl;

import com.group3.sp25hsf302group3se1889vj.controller.common.WebSocketController;
import com.group3.sp25hsf302group3se1889vj.dto.NotificationDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.NotificationFilterDTO;
import com.group3.sp25hsf302group3se1889vj.entity.Notification;
import com.group3.sp25hsf302group3se1889vj.entity.User;
import com.group3.sp25hsf302group3se1889vj.enums.PermissionType;
import com.group3.sp25hsf302group3se1889vj.mapper.NotificationMapper;
import com.group3.sp25hsf302group3se1889vj.repository.NotificationRepository;
import com.group3.sp25hsf302group3se1889vj.repository.PermissionRepository;
import com.group3.sp25hsf302group3se1889vj.repository.UserRepository;
import com.group3.sp25hsf302group3se1889vj.service.NotificationService;
import com.group3.sp25hsf302group3se1889vj.specification.NotificationSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

import java.util.List;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final WebSocketController webSocketController;

    @Override
    public Page<NotificationDTO> findAll(NotificationFilterDTO filterDTO, Pageable pageable) {
        Specification<Notification> specification = NotificationSpecification.filterNotification(filterDTO);
        return notificationRepository.findAll(specification, pageable)
                .map(notificationMapper::toDTO);
    }

    @Override
    public void save(NotificationDTO notificationDTO) {
        notificationRepository.save(notificationMapper.toEntity(notificationDTO));
    }

    @Override
    public NotificationDTO findById(Long id) {
        return notificationRepository.findById(id)
                .map(notificationMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
    }

    @Override
    public void delete(Long id) {
        notificationRepository.deleteById(id);
    }

    @Override
    public void sendNotificationToPermission(NotificationDTO notificationDTO, PermissionType permissionType) {
        var permission = permissionRepository.findByName(permissionType);
        if (permission.isEmpty()) {
            throw new RuntimeException("Permission not found");
        }
        List<User> users = userRepository.findByPermissionsContains(permission.get());
        for (User user : users) {
            Notification notification = notificationMapper.toEntity(notificationDTO);
            notification.setUser(user);
            notificationRepository.save(notification);
            webSocketController.sendMessage(user.getUsername(), notificationMapper.toDTO(notification));
        }
    }

    @Override
    public void sendNotificationToUser(NotificationDTO notificationDTO, String username) {
        var user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        Notification notification = notificationMapper.toEntity(notificationDTO);
        notification.setUser(user.get());
        notificationRepository.save(notification);
        webSocketController.sendMessage(username, notificationMapper.toDTO(notification));
    }

    @Override
    public void markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    @Override
    public int countUnreadNotificationsByUserId(Long id) {
        return notificationRepository.countByUserIdAndIsReadFalse(id);
    }

    @Override
    public NotificationDTO findByIdAndUsername(Long id, String currentUsername) {
        var notification = notificationRepository.findByIdAndUserUsername(id, currentUsername);
        return notification.map(notificationMapper::toDTO).orElse(null);
    }

    @Override
    public void deleteAll(String currentUsername) {
        notificationRepository.deleteAllByUserUsername(currentUsername);
    }

    @Override
    public void markAllAsRead(String currentUsername) {
        List<Notification> notifications = notificationRepository.findAllByUserUsername(currentUsername);
        for (Notification notification : notifications) {
            notification.setRead(true);
        }
        notificationRepository.saveAll(notifications);
    }


}
