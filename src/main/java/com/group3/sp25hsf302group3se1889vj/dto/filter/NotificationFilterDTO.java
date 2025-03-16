package com.group3.sp25hsf302group3se1889vj.dto.filter;

import com.group3.sp25hsf302group3se1889vj.enums.NotificationType;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationFilterDTO extends BaseFilterDTO {
    private String username;
    private NotificationType type;
    private String title;
    private String content;
    private Boolean isRead;
}
