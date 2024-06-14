package com.devcam.shop24h.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devcam.shop24h.entity.Token;
import com.devcam.shop24h.entity.User;
import com.devcam.shop24h.entity.UserProfile;
import com.devcam.shop24h.security.JwtUtil;
import com.devcam.shop24h.security.UserPrincipal;
import com.devcam.shop24h.service.TokenService;
import com.devcam.shop24h.service.UserProfileService;
import com.devcam.shop24h.service.UserService;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/loginUser")
    public ResponseEntity<?> login(@RequestBody User user, HttpServletRequest request) {
        UserPrincipal userPrincipal = userService.findByUsername(user.getUsername());
        if (null == userPrincipal || !new BCryptPasswordEncoder().matches(user.getPassword(), userPrincipal.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false, "message", "Tài khoản hoặc mật khẩu không chính xác"));
        }
        Token token = new Token();
        token.setToken(jwtUtil.generateToken(userPrincipal));
        token.setTokenExpDate(jwtUtil.generateExpirationDate());
        token.setCreatedBy(userPrincipal.getUserId());
        // Kiểm tra xem token có tồn tại không
        if (tokenService.findByToken(token.getToken()) == null) {
            tokenService.createToken(token);

            // Lưu token vào SecurityContextHolder
            Set<GrantedAuthority> authorities = new HashSet<>(userPrincipal.getAuthorities());
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userPrincipal, null, authorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
    
            return ResponseEntity.ok(Map.of("success", true, "token", token.getToken()));
        }
        // Nếu token đã tồn tại trả về luôn
        return ResponseEntity.ok(Map.of("success", true, "token", token.getToken()));
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        return userService.createUser(user);
    }

    @GetMapping("/me")
    public UserProfile getCurrentUser() {
        return userProfileService.setUserProfile();
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        response.setStatus(HttpServletResponse.SC_OK);
    }
}

