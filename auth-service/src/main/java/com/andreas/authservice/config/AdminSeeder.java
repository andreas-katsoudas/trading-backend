package com.andreas.authservice.config;

import com.andreas.authservice.enums.Role;
import com.andreas.authservice.model.User;
import com.andreas.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminSeeder {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    @Value("${roles.admin.password}")
    private String password;

    public AdminSeeder(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @Bean
    public CommandLineRunner seed() {
        return args -> {
            if (repo.findByUsername("admin").isEmpty()) {
                repo.save(User.builder()
                        .username("admin")
                        .password(encoder.encode(password))
                        .role(Role.ADMIN)
                        .build());
            }
        };
    }
}

