package com.devcam.shop24h.api;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devcam.shop24h.entity.Notification;
import com.devcam.shop24h.service.NotificationService;

@RestController
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

   @Autowired
    private SimpMessagingTemplate messagingTemplate;


    @GetMapping("/notifications")
    public Page<Notification> getNotifications(
            @RequestParam String recipient,
            @RequestParam int page,
            @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        return notificationService.getNotifications(recipient, pageable);
    }

    @GetMapping("/notifications/unread")
    public Page<Notification> getUnreadNotifications(
            @RequestParam String recipient,
            @RequestParam int page,
            @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        return notificationService.getUnreadNotifications(recipient, pageable);
    }

    @GetMapping("/notifications/by-date")
    public Page<Notification> getNotificationsByDateRange(
            @RequestParam String recipient,
            @RequestParam Date startDate,
            @RequestParam Date endDate,
            @RequestParam int page,
            @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        return notificationService.getNotificationsByDateRange(recipient, startDate, endDate, pageable);
    }

    // Thông báo cho tất cả mọi người
    @PostMapping("/notifyAll")
    public void notifyAllUsers(@RequestBody Notification notification) {
        notification.setType("general"); // Thiết lập loại thông báo là chung
        notification.setStatus("unread"); // Thiết lập trạng thái thông báo là chưa đọc
        // Lưu thông báo vào cơ sở dữ liệu
        notificationService.saveNotification(notification);
        // Gửi thông báo đến tất cả người dùng
        messagingTemplate.convertAndSend("/all/notifications", notification);
    }

    // Thông báo cho người dùng cụ thể
    @PostMapping("/notify")
    public void notifyUsers(@RequestBody Notification notification, @RequestParam List<String> users) {
        notification.setType("personal"); // Thiết lập loại thông báo là cá nhân
        notification.setStatus("unread"); // Thiết lập trạng thái thông báo là chưa đọc
        // Lưu thông báo vào cơ sở dữ liệu
        notificationService.saveNotification(notification);
        // Gửi thông báo đến từng người dùng
        for (String user : users) {
            messagingTemplate.convertAndSendToUser(user, "/queue/notifications", notification);
        }
    }

    /**
     * Endpoint để gửi thông báo cá nhân
     * @param notification Đối tượng thông báo cần gửi
     * @param userId ID của người nhận thông báo
     */
    @PostMapping("/sendPersonalNotification")
    public void sendPersonalNotification(@RequestBody Notification notification, @RequestParam Long userId) {
        notification.setType("personal");
        notification.setStatus("unread");
        notificationService.sendPersonalNotification(notification, userId);
    }
}
