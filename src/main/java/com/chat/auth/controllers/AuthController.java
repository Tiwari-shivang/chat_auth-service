package com.chat.auth.controllers;
import com.chat.auth.DTOs.ForgotRequest;
import com.chat.auth.DTOs.LoginRequest;
import com.chat.auth.DTOs.ResetPasswordReq;
import com.chat.auth.DTOs.SignUpRequest;
import com.chat.auth.services.AuthService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest request, HttpServletResponse response){
        return ResponseEntity.ok(authService.signUp(request, response));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletResponse response){
        return ResponseEntity.ok(authService.login(request, response));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotRequest request) throws Exception{
        return ResponseEntity.ok(authService.forgotPassword(request));
    }

    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordReq request){
        return ResponseEntity.ok(authService.resetPassword(request));
    }
}
