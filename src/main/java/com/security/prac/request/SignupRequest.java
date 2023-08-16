package com.security.prac.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class SignupRequest {
    @NotEmpty(message = "이름이 비었네요")
    private String name;
    @NotEmpty(message = "email이 비었네요")
    private String email;
    @NotEmpty(message = "비번이 비었네요")
    private String password;
}
