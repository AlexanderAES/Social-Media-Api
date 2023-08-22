package com.socialmediaapi.controller;


import com.socialmediaapi.payload.RegistrationUserDto;
import com.socialmediaapi.payload.request.JwtRequest;
import com.socialmediaapi.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/auth/")
@Tag(name = "Authorization-registration controller", description = "контроллер для авторизации пользователей")
public class AuthController {
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    private final AuthService authService;

    @Operation(summary = "Регистрация пользователя",
            description = "Позволяет пользователю зарегистрироваться.")
    @PostMapping("/signup")
    public ResponseEntity<?> createNewUser(@RequestBody RegistrationUserDto userDto) {
        return authService.createNewUser(userDto);
    }

    @Operation(summary = "Авторизация пользователя",
            description = "Позволяет войти пользователю в сервис.")
    @PostMapping("/signin")
    public ResponseEntity<?> createToken(@RequestBody JwtRequest request) {
        return authService.createToken(request);
    }

}
