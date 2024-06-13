package com.devcam.shop24h.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devcam.shop24h.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
