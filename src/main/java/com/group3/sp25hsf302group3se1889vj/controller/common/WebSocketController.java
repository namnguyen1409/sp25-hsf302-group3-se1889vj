package com.group3.sp25hsf302group3se1889vj.controller.common;

import com.group3.sp25hsf302group3se1889vj.dto.NotificationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendMessage(String username, NotificationDTO notificationDTO) {
        messagingTemplate.convertAndSendToUser(username, "/queue/notifications", notificationDTO);
    }

}









