package com.chat.auth.DTOs;

import com.chat.auth.enums.gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class UpdateProfileReq {
    private String id, firstName, lastName;
    private gender gender;
    private LocalDate dob;
}
