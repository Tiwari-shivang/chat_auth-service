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

    @GetMapping("/{id}")
    public ResponseEntity<?> getMyProfile(@PathVariable String id){
        return ResponseEntity.ok(profileService.getMyProfile(id));
    }

    @PutMapping
    public ResponseEntity<?> updateProfile(@RequestBody UpdateProfileReq request){
        return ResponseEntity.ok(profileService.updateProfile(request));
    }

    @PostMapping("/deactivate")
    public ResponseEntity<?> deactivateProfile(@RequestBody SecuredAPIReq request){
        return ResponseEntity.ok(profileService.deactivateProfile(request));
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteProfile(@RequestBody SecuredAPIReq request){
        return ResponseEntity.ok(profileService.deleteAccount(request));
    }
}
