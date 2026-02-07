package com.chat.auth.DTOs;

import com.chat.auth.enums.gender;
import com.chat.auth.enums.roles;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {
    private String firstName, lastName, userName, email, password;
    private roles role;
    private gender gender;
    private LocalDate dob;
}
