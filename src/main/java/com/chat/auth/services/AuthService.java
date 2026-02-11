package com.chat.auth.services;

import com.chat.auth.DTOs.*;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    SuccessResponse signUp(SignUpRequest request, HttpServletResponse response);
    SuccessResponse login(LoginRequest request, HttpServletResponse response);
    SuccessResponse forgotPassword(ForgotRequest request) throws MessagingException;
    SuccessResponse resetPassword(ResetPasswordReq request);
}
