package com.devcam.shop24h.api;

import com.devcam.shop24h.entity.HelloMessage;
import com.devcam.shop24h.entity.NotificationMessage;
import com.devcam.shop24h.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // @MessageMapping("/notify")
    // public void sendNotification(NotificationMessage message) {
    //     notificationService.sendNotification(message.getUserId(), message.getContent());
    // }

    @MessageMapping("/notify")
    @SendTo("/topic/notifications")
    public String sendNotification(String message) {
        return message;
    }
    @MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public NotificationMessage greeting(HelloMessage message) throws Exception {
		Thread.sleep(1000); // simulated delay
		return new NotificationMessage("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
	}
}
