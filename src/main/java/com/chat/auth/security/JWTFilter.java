package com.chat.auth.security;

import com.chat.auth.models.Users;
import com.chat.auth.services.SecurityService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {
    @Autowired
    private JWTUtils utils;
    @Autowired
    private SecurityService securityService;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filter) throws ServletException, IOException {
        if(request.getServletPath().startsWith("/auth")){
            filter.doFilter(request, response);
            return;
        }
        String token = request.getHeader("Authorization").substring(7);
        UserDetails securityModel = securityService.loadUserByUsername(utils.extractEmail(token));
        Users user = (Users) securityModel;
        if(SecurityContextHolder.getContext().getAuthentication() == null && utils.validateToken(token, user)){
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, securityModel.getAuthorities()));
            filter.doFilter(request, response);
        }
    }
}
