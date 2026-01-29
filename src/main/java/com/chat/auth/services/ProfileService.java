package com.chat.auth.services;

import com.chat.auth.DTOs.UpdateProfileReq;
import com.chat.auth.DTOs.UserResponse;

public interface ProfileService {
    UserResponse getMyProfile(String userId);
    UserResponse updateProfile(UpdateProfileReq request);
}
