package com.chat.auth.services.impl;

import com.chat.auth.DTOs.LoginRequest;
import com.chat.auth.DTOs.LoginResponse;
import com.chat.auth.DTOs.SignUpRequest;
import com.chat.auth.DTOs.UserResponse;
import com.chat.auth.models.Users;
import com.chat.auth.repositories.userRepository;
import com.chat.auth.security.JWTUtils;
import com.chat.auth.services.AuthService;
import com.chat.auth.services.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;

@Service
public class AuthService_Impl implements AuthService {
    @Autowired
    private userRepository userRepo;
    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private JWTUtils utils;
    public UserResponse signUp(SignUpRequest request){
        Users user = new Users();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUserName(request.getUserName());
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setGender(request.getGender());
        user.setDob(request.getDob());
        user.setIsActive(true);
        user.setLastLogin(new Timestamp(System.currentTimeMillis()));
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        userRepo.save(user);
        return new UserResponse(user.getUid().toString(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail(), user.getRole(), user.getGender(), user.getDob(), user.getIsActive());
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        Authentication auth = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        Users user = (Users) manager.authenticate(auth).getPrincipal();
        assert user != null;
        return new LoginResponse(utils.buildToken(user));
    }
}
