package com.group3.sp25hsf302group3se1889vj.service.impl;

import com.group3.sp25hsf302group3se1889vj.dto.NotificationDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.NotificationFilterDTO;
import com.group3.sp25hsf302group3se1889vj.entity.Notification;
import com.group3.sp25hsf302group3se1889vj.mapper.NotificationMapper;
import com.group3.sp25hsf302group3se1889vj.repository.NotificationRepository;
import com.group3.sp25hsf302group3se1889vj.service.NotificationService;
import com.group3.sp25hsf302group3se1889vj.specification.NotificationSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

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
    public void update(NotificationDTO notificationDTO) {
        Notification entity = notificationRepository.findById(notificationDTO.getId())
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        entity.setTitle(notificationDTO.getTitle());
        entity.setContent(notificationDTO.getContent());
        notificationRepository.save(entity);
    }

    @Override
    public void delete(Long id) {
        notificationRepository.deleteById(id);
    }
}
