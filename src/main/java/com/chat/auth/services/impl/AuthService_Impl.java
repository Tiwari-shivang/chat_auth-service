package com.chat.auth.services.impl;

import com.chat.auth.DTOs.*;
import com.chat.auth.helpers.MailSenderCustom;
import com.chat.auth.helpers.ShortLivedToken;
import com.chat.auth.models.Users;
import com.chat.auth.repositories.userRepository;
import com.chat.auth.security.JWTUtils;
import com.chat.auth.services.AuthService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class AuthService_Impl implements AuthService {
    @Autowired
    private JavaMailSender sender;
    @Autowired
    private userRepository userRepo;
    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private JWTUtils utils;
    @Autowired
    private MailSenderCustom senderCustom;

    @Value("${client.url}")
    private String clientUrl;

    @Override
    public SuccessResponse signUp(SignUpRequest request, HttpServletResponse response){
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
        user.setIsVerified(false);
        user.setLastLogin(new Timestamp(System.currentTimeMillis()));
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        Users newUser = userRepo.save(user);
        ResponseCookie cookie = buildAuthCookie(newUser);
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return new SuccessResponse("Account created successfully");
    }

    @Override
    public SuccessResponse login(LoginRequest request, HttpServletResponse response) {
        Authentication auth = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        Users user = (Users) manager.authenticate(auth).getPrincipal();
        assert user != null;
        user.setLastLogin(new Timestamp(System.currentTimeMillis()));
        Users loggedInUser = userRepo.save(user);
        response.addHeader(HttpHeaders.SET_COOKIE, buildAuthCookie(loggedInUser).toString());
        return new SuccessResponse("User logged in successfully");
    }

    @Override
    public SuccessResponse forgotPassword(ForgotRequest request) throws MessagingException {
        Users user = userRepo.findByEmail(request.getEmail());
        if(user == null){
            throw new RuntimeException("User not found");
        }
        ShortLivedToken shortLivedToken = new ShortLivedToken();
        String token = shortLivedToken.createShortLivedToken(request.getEmail());
        String subject = "Password Reset Request";
        String content = """
                <html>
                <body>
                <h2 style="color: blue; font-family: Arial;">
                Reset Your Password
                </h2>
                
                
                <p style="font-size:14px; color:#333;">
                Click the button below to reset your password.
                </p>
                
                
                <a href="http://localhost:5173/reset-password?user=%s"
                style="
                background:#4CAF50;
                color:white;
                padding:10px 15px;
                text-decoration:none;
                border-radius:5px;
                display:inline-block;">
                Reset Password
                </a>
                </body>
                </html>
                """.formatted(token);
        senderCustom.setTo(request.getEmail());
        senderCustom.setSubject(subject);
        senderCustom.setContent(content);
        senderCustom.sendMail();
        return new SuccessResponse("Mail sent");
    }

    @Override
    public SuccessResponse resetPassword(ResetPasswordReq request){
        ShortLivedToken shortLivedToken = new ShortLivedToken();
        String email = shortLivedToken.extractEmail(request.getShortLivedToken());
        if(shortLivedToken.validateShortLivedToken(email, request.getShortLivedToken())){
            Users user = userRepo.findByEmail(email);
            user.setPassword(encoder.encode(request.getNewPassword()));
            user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            userRepo.save(user);
            return new SuccessResponse("Password updated");
        }
        throw new RuntimeException();
    }

    private ResponseCookie buildAuthCookie(Users user){
        String token = utils.buildToken(user);
        return ResponseCookie.from("access_token", token)
        .httpOnly(true)
        .maxAge(60*60)
        .sameSite("Strict")
        .path("/")
        .secure(false)
        .build();
    }
}
