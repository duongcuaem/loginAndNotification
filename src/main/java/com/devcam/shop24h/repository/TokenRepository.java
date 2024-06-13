package com.devcam.shop24h.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devcam.shop24h.entity.Token;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Token findByToken(String token);
}
