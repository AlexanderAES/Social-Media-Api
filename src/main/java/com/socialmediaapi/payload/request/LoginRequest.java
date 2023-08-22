package com.socialmediaapi.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotEmpty;

public class LoginRequest {
    @NotEmpty(message = "Username can't be empty")
    @Schema(name = "username", description = "для входа использовать email")
    private String username;
    @NotEmpty(message = "Password can't be empty")
    private String password;

    public LoginRequest() {  }

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
