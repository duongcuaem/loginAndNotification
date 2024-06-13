package com.devcam.shop24h.repository;

import com.devcam.shop24h.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    UserProfile findByUserId(Long userId);
    UserProfile findByUsername(String username);
    UserProfile findBySocialcode(String code);
}
