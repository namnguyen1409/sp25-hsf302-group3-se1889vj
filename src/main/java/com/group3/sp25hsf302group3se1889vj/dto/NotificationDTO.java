package com.group3.sp25hsf302group3se1889vj.dto;

import com.group3.sp25hsf302group3se1889vj.entity.User;
import com.group3.sp25hsf302group3se1889vj.enums.NotificationType;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor

public class NotificationDTO extends BaseDTO {
    private User user;

    private NotificationType type;

    private String title;

    private String content;

    private boolean isRead = false;
}
