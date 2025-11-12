package com.andreas.authservice.model;

import lombok.Data;

public record AuthRequest (
        String username,
        String password
){
}
