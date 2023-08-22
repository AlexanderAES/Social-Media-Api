package com.socialmediaapi.service;

import com.socialmediaapi.payload.RegistrationUserDto;
import com.socialmediaapi.payload.request.JwtRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<?> createNewUser(RegistrationUserDto userDto);
    ResponseEntity<?> createToken(JwtRequest request);
}
