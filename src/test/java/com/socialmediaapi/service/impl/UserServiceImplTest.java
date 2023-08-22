package com.socialmediaapi.service.impl;

import com.socialmediaapi.model.Role;
import com.socialmediaapi.model.User;
import com.socialmediaapi.payload.RegistrationUserDto;
import com.socialmediaapi.repository.FriendRequestRepository;
import com.socialmediaapi.repository.SubscriptionRepository;
import com.socialmediaapi.repository.UserRepository;
import com.socialmediaapi.service.RoleService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Principal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    User currentUser;
    @Mock
    private FriendRequestRepository friendRequestRepository;  // мок репозитория FriendRequestRepository

    @Mock
    private UserRepository userRepository;

    @Mock
    private Principal principal;  // мок Principal, используемый для передачи текущего пользователя

    @Mock
    private RoleService roleService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RegistrationUserDto registrationUserDto;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @InjectMocks
    UserServiceImpl userServiceImpl;

    @Test
    void createNewUser_ShouldSaveUserToRepository() {
        // Arrange
        RegistrationUserDto userDto = new RegistrationUserDto("testUser", "test@example.com", "password","password");
        Role role_user = new Role(1,"USER");
        User expectedUser = new User("testUser", "test@example.com", "encodedPassword", List.of(role_user));

        when(roleService.getUserRole()).thenReturn(role_user);
        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encodedPassword");

        // Act
        userServiceImpl.createNewUser(userDto);

        // Assert
        verify(roleService, times(1)).getUserRole();
        verify(passwordEncoder, times(1)).encode(userDto.getPassword());
        verify(userRepository, times(1)).save(expectedUser);
    }

    @Test
    void createNewUser_ShouldUseCorrectUserRole() {
        // Arrange
        RegistrationUserDto userDto = new RegistrationUserDto("testUser", "test@example.com", "password","password");
        Role expectedRole = new Role(1,"USER");

        when(roleService.getUserRole()).thenReturn(expectedRole);

        // Act
        userServiceImpl.createNewUser(userDto);

        // Assert
        verify(roleService, times(1)).getUserRole();
    }

    @Test
    void createNewUser_ShouldEncodePassword() {
        // Arrange
        RegistrationUserDto userDto = new RegistrationUserDto("testUser", "test@example.com", "password","password");
        String expectedEncodedPassword = "encodedPassword";

        when(passwordEncoder.encode(userDto.getPassword())).thenReturn(expectedEncodedPassword);

        // Act
        userServiceImpl.createNewUser(userDto);

        // Assert
        verify(passwordEncoder, times(1)).encode(userDto.getPassword());
    }
}