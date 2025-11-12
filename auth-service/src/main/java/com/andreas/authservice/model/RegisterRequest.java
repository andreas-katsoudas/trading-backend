package com.andreas.authservice.model;

public record RegisterRequest(
        String username,
        String password
) {
}
