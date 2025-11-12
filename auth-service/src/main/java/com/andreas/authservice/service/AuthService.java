package com.andreas.authservice.service;

import com.andreas.authservice.enums.Role;
import com.andreas.authservice.exception.InvalidPasswordException;
import com.andreas.authservice.exception.UserAlreadyExistsException;
import com.andreas.authservice.exception.UserNotFoundException;
import com.andreas.authservice.model.AuthRequest;
import com.andreas.authservice.model.AuthResponse;
import com.andreas.authservice.model.User;
import com.andreas.authservice.repository.UserRepository;
import com.andreas.authservice.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    public void register(String username, String password) {
        if(userRepository.findByUsername(username).isPresent()){
            throw new UserAlreadyExistsException("User already exists");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.USER);
        userRepository.save(user);
    }

    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if(!passwordEncoder.matches(request.password(), user.getPassword())){
            throw new InvalidPasswordException("Username or password are wrong");
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        return new AuthResponse(token);
    }


}
