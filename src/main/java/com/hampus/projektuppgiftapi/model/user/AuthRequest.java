package com.hampus.projektuppgiftapi.model.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthRequest {

    @NotBlank
    @Size(min = 5, max = 20, message = "Username must be between 5 and 20 characters")
    private String username;

    @NotBlank
    @Size(min = 5, message = "Password must be at least 5 characters")
    private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(@NotBlank @Size(min = 4, max = 20) String username) {
        this.username = username;
    }

    public void setPassword(@NotBlank @Size(min = 4) String password) {
        this.password = password;
    }
}

