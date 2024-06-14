package com.devcam.shop24h.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.devcam.shop24h.entity.Notification;
import com.devcam.shop24h.repository.NotificationRepository;

import java.util.Date;

@Service
public class NotificationService {

    // Tự động liên kết với NotificationRepository
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate; // Thêm phần này để gửi tin nhắn qua WebSocket

    /**
     * Phương thức lưu thông báo vào cơ sở dữ liệu
     * @param notification Đối tượng Notification cần lưu
     */
    public void saveNotification(Notification notification) {
        notificationRepository.save(notification);
    }

    /**
     * Phương thức lấy danh sách thông báo của người dùng bao gồm cả thông báo chung và thông báo riêng với phân trang
     * @param recipient Tên người nhận thông báo
     * @param pageable Thông tin phân trang
     * @return Trang của các thông báo
     */
    public Page<Notification> getNotifications(String recipient, Pageable pageable) {
        return notificationRepository.findAllByRecipientOrGeneral(recipient, pageable);
    }

    /**
     * Phương thức lấy danh sách thông báo chưa đọc của người dùng bao gồm cả thông báo chung và thông báo riêng với phân trang
     * @param recipient Tên người nhận thông báo
     * @param pageable Thông tin phân trang
     * @return Trang của các thông báo chưa đọc
     */
    public Page<Notification> getUnreadNotifications(String recipient, Pageable pageable) {
        return notificationRepository.findAllByRecipientOrGeneralAndStatus(recipient, "unread", pageable);
    }

    /**
     * Phương thức lấy danh sách thông báo của người dùng trong khoảng thời gian xác định bao gồm cả thông báo chung và thông báo riêng với phân trang
     * @param recipient Tên người nhận thông báo
     * @param startDate Ngày bắt đầu
     * @param endDate Ngày kết thúc
     * @param pageable Thông tin phân trang
     * @return Trang của các thông báo trong khoảng thời gian xác định
     */
    public Page<Notification> getNotificationsByDateRange(String recipient, Date startDate, Date endDate, Pageable pageable) {
        return notificationRepository.findAllByRecipientOrGeneralAndDateRange(recipient, startDate, endDate, pageable);
    }

    /**
     * Phương thức gửi thông báo cá nhân đến người dùng cụ thể
     * @param notification Đối tượng thông báo cần gửi
     * @param userId ID của người nhận thông báo
     */
    public void sendPersonalNotification(Notification notification, Long userId) {
        // Lưu thông báo vào cơ sở dữ liệu
        saveNotification(notification);
        // Gửi thông báo đến người dùng qua WebSocket
        messagingTemplate.convertAndSendToUser(notification.getRecipient().toString(), "/specific/notifications", notification);
    }
}
