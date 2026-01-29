package com.chat.auth.services;

import com.chat.auth.DTOs.*;
import jakarta.mail.MessagingException;

public interface AuthService {
    UserResponse signUp(SignUpRequest request);
    LoginResponse login(LoginRequest request);
    SuccessResponse forgotPassword(ForgotRequest request) throws MessagingException;
}
