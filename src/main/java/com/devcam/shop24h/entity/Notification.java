package com.devcam.shop24h.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID duy nhất của thông báo, tự động tăng

    private String message; // Nội dung của thông báo
    private String recipient; // Người nhận thông báo, có thể là tên người dùng hoặc ID
    private String category; // Chuyên mục của thông báo
    private String type; // Loại thông báo: 'general' cho thông báo chung, 'personal' cho thông báo cá nhân
    private String status; // Trạng thái của thông báo: 'unread' (chưa đọc), 'read' (đã đọc), v.v.

    public Notification() {
    }

    public Notification(String message, String recipient, String category, String type, String status) {
        this.message = message;
        this.recipient = recipient;
        this.category = category;
        this.type = type;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
