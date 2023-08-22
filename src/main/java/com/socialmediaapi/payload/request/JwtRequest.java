package com.socialmediaapi.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Сущность Login, получение JWT токена")
@Data
public class JwtRequest {
    @Schema(description = "Имя пользователя")
    private String username;
    @Schema(description = "Пароль")
    private String password;
}
