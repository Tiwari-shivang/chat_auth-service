package com.chat.auth.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SecuredAPIReq {
    private String Password;
    private LocalDate dob;
}
