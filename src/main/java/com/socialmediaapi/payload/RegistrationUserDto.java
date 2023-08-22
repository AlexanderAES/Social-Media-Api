package com.socialmediaapi.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Сущность регистрации пользователя")
@Data
@NoArgsConstructor
public class RegistrationUserDto {

    @Schema(description = "имя пользователя")
    private String username;
    @Schema(description = "email пользователя")
    private String email;
    @Schema(description = "пароль")
    private String password;
    @Schema(description = "повтор пароля")
    private String confirmPassword;

    public RegistrationUserDto(String username, String email, String password, String confirmPassword) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }
}
