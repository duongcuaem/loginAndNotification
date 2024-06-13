package com.devcam.shop24h.service;

import com.devcam.shop24h.entity.Token;

public interface TokenService {

    Token createToken(Token token);

    Token findByToken(String token);
}
