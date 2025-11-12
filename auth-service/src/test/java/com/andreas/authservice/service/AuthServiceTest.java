package com.andreas.authservice.service;

import com.andreas.authservice.enums.Role;
import com.andreas.authservice.exception.InvalidPasswordException;
import com.andreas.authservice.exception.UserAlreadyExistsException;
import com.andreas.authservice.exception.UserNotFoundException;
import com.andreas.authservice.model.AuthRequest;
import com.andreas.authservice.model.User;
import com.andreas.authservice.repository.UserRepository;
import com.andreas.authservice.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerTrue() {
        when(userRepository.findByUsername("john")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("pass")).thenReturn("encodedPass");

        authService.register("john", "pass");

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerUserAlreadyExists() {
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(new User()));

        assertThatThrownBy(() -> authService.register("john", "pass"))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessage("User already exists");
    }

    @Test
    void loginTrue() {
        User user = new User();
        user.setUsername("john");
        user.setPassword("encodedPass");
        user.setRole(Role.USER);

        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("pass", "encodedPass")).thenReturn(true);
        when(jwtUtil.generateToken("john", Role.USER)).thenReturn("jwtToken");

        authService.login(new AuthRequest("john", "pass"));

        verify(jwtUtil, times(1)).generateToken("john", Role.USER);
    }

    @Test
    void loginUserNotFound() {
        when(userRepository.findByUsername("john")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(new AuthRequest("john", "pass")))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found");
    }

    @Test
    void loginInvalidPassword() {
        User user = new User();
        user.setUsername("john");
        user.setPassword("encodedPass");

        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPass", "encodedPass")).thenReturn(false);

        assertThatThrownBy(() -> authService.login(new AuthRequest("john", "wrongPass")))
                .isInstanceOf(InvalidPasswordException.class)
                .hasMessage("Username or password are wrong");
    }
}
