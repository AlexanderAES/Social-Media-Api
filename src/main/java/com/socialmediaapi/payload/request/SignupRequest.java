package com.socialmediaapi.payload.request;

import com.socialmediaapi.annotations.PasswordMatches;
import com.socialmediaapi.annotations.ValidEmail;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Schema(description = "Сущность регистрация")
@PasswordMatches
public class SignupRequest {
    @NotEmpty(message = "Please enter your username")
    @Size(min = 5, max = 100)
    private String username;
    @Email(message = "It should have email format")
    @NotBlank(message = "User email is required")
    @ValidEmail
    private String email;
    @NotEmpty(message = "password is required")
    @Size(min = 8, max = 100)
    private String password;
    private String confirmPassword;

    public SignupRequest() {
    }

    public SignupRequest(String username, String email, String password, String confirmPassword) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
