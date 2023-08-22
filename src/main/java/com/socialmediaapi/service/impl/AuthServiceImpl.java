package com.socialmediaapi.service.impl;


import com.socialmediaapi.exceptions.AppError;
import com.socialmediaapi.payload.RegistrationUserDto;
import com.socialmediaapi.payload.request.JwtRequest;
import com.socialmediaapi.payload.response.JwtResponse;
import com.socialmediaapi.payload.response.MessageResponse;
import com.socialmediaapi.security.JwtTokenProvider;
import com.socialmediaapi.service.AuthService;
import com.socialmediaapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import static com.socialmediaapi.security.SecurityConstants.TOKEN_PREFIX;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthServiceImpl(UserService userService,
                           JwtTokenProvider jwtTokenProvider,
                           AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Метод используется для регистрации нового пользователя.
     *
     * @param userDto данные нового пользователя.
     * @return ResponseEntity в качестве ответа на запрос.
     */
    @Override
    public ResponseEntity<?> createNewUser(RegistrationUserDto userDto) {
        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            return new ResponseEntity<>(new AppError(BAD_REQUEST.value(), "Пароли не совпадают"), BAD_REQUEST);
        }
        if (userService.findByUsername(userDto.getUsername()).isPresent()) {
            return new ResponseEntity<>(new AppError(BAD_REQUEST.value(), "Пользователь с указанным email уже существует"), BAD_REQUEST);
        }
        userService.createNewUser(userDto);
        return ResponseEntity.ok(new MessageResponse("пользователь успешно зарегистрирован"));
    }


    /**
     * Метод используется для создания и выдачи JWT токена при авторизации пользователя.
     *
     * @param request - объект JwtRequest, который содержит логин и пароль пользователя.
     * @return возвращается объект ResponseEntity с кодом HttpStatus.OK и объектом
     * JwtResponse, содержащим сгенерированный токен.
     */
    @Override
    public ResponseEntity<?> createToken(JwtRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), "Неверный логин или пароль"), HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = userService.loadUserByUsername(request.getUsername());
        String token = TOKEN_PREFIX + jwtTokenProvider.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }
}
