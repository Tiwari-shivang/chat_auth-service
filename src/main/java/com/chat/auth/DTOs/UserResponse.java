package com.chat.auth.DTOs;

import com.chat.auth.enums.gender;
import com.chat.auth.enums.roles;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserResponse {
    private String id, firstName, lastName, userName, email;
    private roles role;
    private gender gender;
    private LocalDate dob;
    private Boolean isActive;
}
