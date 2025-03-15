package com.group3.sp25hsf302group3se1889vj.mapper;

import com.group3.sp25hsf302group3se1889vj.dto.NotificationDTO;
import com.group3.sp25hsf302group3se1889vj.entity.Notification;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationMapper {
    private final ModelMapper modelMapper;

    public NotificationDTO toDTO(Notification entity) {
        return modelMapper.map(entity, NotificationDTO.class);
    }

    public Notification toEntity(NotificationDTO dto) {
        return modelMapper.map(dto, Notification.class);
    }
}
