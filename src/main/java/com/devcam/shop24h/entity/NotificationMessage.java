package com.devcam.shop24h.entity;

public class NotificationMessage {

    private String userId;
    private String content;

    public NotificationMessage() {}

    public NotificationMessage(String content) {
        this.content = content;
    }

    public NotificationMessage(String userId, String content) {
        this.userId = userId;
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
