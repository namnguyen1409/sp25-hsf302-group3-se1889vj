package com.group3.sp25hsf302group3se1889vj.dto;

import com.group3.sp25hsf302group3se1889vj.entity.User;
import com.group3.sp25hsf302group3se1889vj.enums.NotificationType;
import com.group3.sp25hsf302group3se1889vj.util.FieldMetadata;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO extends BaseDTO {
    @FieldMetadata(title = "Người nhận")
    private String username;
    @FieldMetadata(title = "Loại thông báo", cssClass = "notification-type")
    private NotificationType type;
    @FieldMetadata(title = "Tiêu đề")
    private String title;
    @FieldMetadata(title = "Nội dung", cssClass = "description")
    private String content;
    @FieldMetadata(title = "Đã đọc", cssClass = "boolean")
    private boolean isRead = false;
}
