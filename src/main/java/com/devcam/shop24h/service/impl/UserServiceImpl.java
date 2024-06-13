package com.devcam.shop24h.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.devcam.shop24h.entity.User;
import com.devcam.shop24h.repository.UserRepository;
import com.devcam.shop24h.security.UserPrincipal;
import com.devcam.shop24h.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User createUser(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Override
    public UserPrincipal findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        UserPrincipal userPrincipal = null;

        if (null != user) {
            Set<SimpleGrantedAuthority> authorities = new HashSet<>();
            if (null != user.getRoles()) {
                user.getRoles().forEach(r -> {
                    authorities.add(new SimpleGrantedAuthority(r.getRoleKey()));
                    r.getPermissions().forEach(p -> authorities.add(new SimpleGrantedAuthority(p.getPermissionKey())));
                });
            }

            userPrincipal = new UserPrincipal(user.getId(), user.getUsername(), user.getPassword(), authorities);
        }
        return userPrincipal;
    }

}
