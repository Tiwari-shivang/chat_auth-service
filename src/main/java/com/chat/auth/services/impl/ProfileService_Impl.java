package com.chat.auth.services.impl;

import com.chat.auth.DTOs.UpdateProfileReq;
import com.chat.auth.DTOs.UserResponse;
import com.chat.auth.models.Users;
import com.chat.auth.repositories.userRepository;
import com.chat.auth.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProfileService_Impl implements ProfileService {

    @Autowired
    private userRepository userRepo;

    @Override
    public UserResponse getMyProfile(String userId){
        Users user = userRepo.findById(UUID.fromString(userId)).orElseThrow(() -> new RuntimeException("Profile not found"));
        return new UserResponse(user.getUid().toString(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail(), user.getRole(), user.getGender(), user.getDob(), user.getIsActive());
    }

    @Override
    public UserResponse updateProfile(UpdateProfileReq request){
        Users user = userRepo.findById(UUID.fromString(request.getId())).orElseThrow(() -> new RuntimeException("User not found"));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setGender(request.getGender());
        user.setDob(request.getDob());
        userRepo.save(user);
        return new UserResponse(user.getUid().toString(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail(), user.getRole(), user.getGender(), user.getDob(), user.getIsActive());
    }
}
