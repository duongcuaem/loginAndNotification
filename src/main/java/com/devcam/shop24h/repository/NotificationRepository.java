package com.devcam.shop24h.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.devcam.shop24h.entity.Notification;

import java.util.Date;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Lấy thông báo của người dùng với phân trang
    @Query("SELECT n FROM Notification n WHERE n.recipient = :recipient OR n.type = 'general' ORDER BY n.createdAt DESC")
    Page<Notification> findAllByRecipientOrGeneral(@Param("recipient") String recipient, Pageable pageable);

    // Lấy thông báo của người dùng với phân trang và lọc theo trạng thái chưa đọc
    @Query("SELECT n FROM Notification n WHERE (n.recipient = :recipient OR n.type = 'general') AND n.status = :status ORDER BY n.createdAt DESC")
    Page<Notification> findAllByRecipientOrGeneralAndStatus(@Param("recipient") String recipient, @Param("status") String status, Pageable pageable);

    // Lấy thông báo của người dùng với phân trang và lọc theo ngày
    @Query("SELECT n FROM Notification n WHERE (n.recipient = :recipient OR n.type = 'general') AND n.createdAt >= :startDate AND n.createdAt <= :endDate ORDER BY n.createdAt DESC")
    Page<Notification> findAllByRecipientOrGeneralAndDateRange(@Param("recipient") String recipient, @Param("startDate") Date startDate, @Param("endDate") Date endDate, Pageable pageable);
}
