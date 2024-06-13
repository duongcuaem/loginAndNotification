package com.devcam.shop24h.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationRestController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/api/notify")
    public void notify(@RequestBody String message) {
        simpMessagingTemplate.convertAndSend("/topic/notifications", message);
    }
}
