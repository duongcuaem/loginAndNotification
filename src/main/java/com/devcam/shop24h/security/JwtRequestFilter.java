package com.devcam.shop24h.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.devcam.shop24h.entity.Token;
import com.devcam.shop24h.service.TokenService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenService verificationTokenService;

    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        String jwt = null;
        UserPrincipal user = null;

        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Token ")) {
            jwt = authorizationHeader.substring(6);
            user = jwtUtil.getUserFromToken(jwt);
            System.out.println("JWT: " + jwt);
            System.out.println("Username: " + user);
        }

        if (user != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            Token token = verificationTokenService.findByToken(jwt);
            if (token != null && token.getTokenExpDate().after(new Date())) {
                Set<GrantedAuthority> authorities = new HashSet<>(user.getAuthorities());

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(user, null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}
