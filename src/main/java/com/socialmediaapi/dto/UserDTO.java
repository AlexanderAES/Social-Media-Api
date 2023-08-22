package com.socialmediaapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Schema(description = "Информация о пользователе")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    @Schema(description = "Идентификатор пользователя")
    private Long id;
    @NotEmpty
    @Schema(description = "Имя пользователя")
    private String username;
    @Schema(description = "email")
    @NotEmpty
    private String email;
}
