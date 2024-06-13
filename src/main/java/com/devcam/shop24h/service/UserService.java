package com.devcam.shop24h.service;

import com.devcam.shop24h.entity.User;
import com.devcam.shop24h.security.UserPrincipal;

public interface UserService {
    User createUser(User user);

    UserPrincipal findByUsername(String username);
}
