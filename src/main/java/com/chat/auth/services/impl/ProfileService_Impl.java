package com.chat.auth.services.impl;

import com.chat.auth.DTOs.SecuredAPIReq;
import com.chat.auth.DTOs.SuccessResponse;
import com.chat.auth.DTOs.UpdateProfileReq;
import com.chat.auth.DTOs.UserResponse;
import com.chat.auth.helpers.MailSenderCustom;
import com.chat.auth.helpers.ShortLivedToken;
import com.chat.auth.models.Users;
import com.chat.auth.repositories.userRepository;
import com.chat.auth.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProfileService_Impl implements ProfileService {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private userRepository userRepo;

    @Override
    public UserResponse getMyProfile() {
        Users user = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new UserResponse(user.getUid().toString(), user.getFirstName(), user.getLastName(), user.getUsername(),
                user.getEmail(), user.getRole(), user.getGender(), user.getDob(), user.getIsActive(),
                user.getIsVerified());
    }

    @Override
    public UserResponse updateProfile(UpdateProfileReq request) {
        Users user = userRepo.findById(UUID.fromString(request.getId()))
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setGender(request.getGender());
        user.setDob(request.getDob());
        userRepo.save(user);
        return new UserResponse(user.getUid().toString(), user.getFirstName(), user.getLastName(), user.getUsername(),
                user.getEmail(), user.getRole(), user.getGender(), user.getDob(), user.getIsActive(),
                user.getIsVerified());
    }

    @Override
    public SuccessResponse deactivateProfile(SecuredAPIReq request) {
        Users user = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(), request.getPassword());
        manager.authenticate(auth);
        if (user.getDob().equals(request.getDob())) {
            user.setIsActive(false);
            userRepo.save(user);
            return new SuccessResponse("Done");
        }
        throw new RuntimeException();
    }

    @Override
    public SuccessResponse deleteAccount(SecuredAPIReq request) {
        Users user = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(), request.getPassword());
        manager.authenticate(auth);
        if (user.getDob().equals(request.getDob())) {
            userRepo.deleteById(user.getUid());
            return new SuccessResponse("Done");
        }
        throw new RuntimeException();
    }

    public SuccessResponse verificationRequest() {
        Users user = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MailSenderCustom mailSender = new MailSenderCustom();
        mailSender.setTo(user.getEmail());
        ShortLivedToken tokenCraeter = new ShortLivedToken();
        String shortLivedToken = tokenCraeter.createShortLivedToken(user.getEmail());
        mailSender.setTo(user.getEmail());
        mailSender.setContent("""
                <html>
                <body>
                <h2 style="color: blue; font-family: Arial;">
                Reset Your Password
                </h2>


                <p style="font-size:14px; color:#333;">
                Click the button below to reset your password.
                </p>


                <a href="http://localhost:5173/verify-email?identity=%s"
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
                """.formatted(shortLivedToken));
        mailSender.setSubject("Click to verify email");
        mailSender.sendMail();
        return new SuccessResponse("Mail sent successfully!");
    }
}
