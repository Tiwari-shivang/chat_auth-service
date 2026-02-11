package com.chat.auth.services.impl;

import com.chat.auth.DTOs.SecuredAPIReq;
import com.chat.auth.DTOs.SuccessResponse;
import com.chat.auth.DTOs.UpdateProfileReq;
import com.chat.auth.DTOs.UserResponse;
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
    AuthenticationManager manager;

    @Autowired
    private userRepository userRepo;

    @Override
    public UserResponse getMyProfile(String userId){
        Users user = userRepo.findById(UUID.fromString(userId)).orElseThrow(() -> new RuntimeException("Profile not found"));
        return new UserResponse(user.getUid().toString(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail(), user.getRole(), user.getGender(), user.getDob(), user.getIsActive(), user.getIsVerified());
    }

    @Override
    public UserResponse updateProfile(UpdateProfileReq request){
        Users user = userRepo.findById(UUID.fromString(request.getId())).orElseThrow(() -> new RuntimeException("User not found"));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setGender(request.getGender());
        user.setDob(request.getDob());
        userRepo.save(user);
        return new UserResponse(user.getUid().toString(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail(), user.getRole(), user.getGender(), user.getDob(), user.getIsActive(), user.getIsVerified());
    }

    @Override
    public SuccessResponse deactivateProfile(SecuredAPIReq request){
        Users user = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(), request.getPassword());
        manager.authenticate(auth);
        if(user.getDob().equals(request.getDob())){
            user.setIsActive(false);
            userRepo.save(user);
            return new SuccessResponse("Done");
        }
        throw new RuntimeException();
    }

    @Override
    public SuccessResponse deleteAccount(SecuredAPIReq request){
        Users user = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(), request.getPassword());
        manager.authenticate(auth);
        if(user.getDob().equals(request.getDob())){
            userRepo.deleteById(user.getUid());
            return new SuccessResponse("Done");
        }
        throw new RuntimeException();
    }
}
