package com.chat.auth.controllers;

import com.chat.auth.DTOs.SecuredAPIReq;
import com.chat.auth.DTOs.UpdateProfileReq;
import com.chat.auth.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @GetMapping
    public ResponseEntity<?> getMyProfile() {
        return ResponseEntity.ok(profileService.getMyProfile());
    }

    @PutMapping
    public ResponseEntity<?> updateProfile(@RequestBody UpdateProfileReq request) {
        return ResponseEntity.ok(profileService.updateProfile(request));
    }

    @PostMapping("/deactivate")
    public ResponseEntity<?> deactivateProfile(@RequestBody SecuredAPIReq request) {
        return ResponseEntity.ok(profileService.deactivateProfile(request));
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteProfile(@RequestBody SecuredAPIReq request) {
        return ResponseEntity.ok(profileService.deleteAccount(request));
    }

    @PutMapping("/verify-account")
    public ResponseEntity<?> verifyAccount(@RequestParam String token) {
        return ResponseEntity.ok("Verified!!");
    }

    @GetMapping("/verification-request")
    public ResponseEntity<?> verifyEmail() {
        return ResponseEntity.ok(profileService.verificationRequest());
    }
}
