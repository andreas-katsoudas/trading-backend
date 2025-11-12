package com.andreas.authservice.service;

import com.andreas.authservice.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class AdminService {

    private final UserRepository repository;

    public AdminService(UserRepository repository){
        this.repository = repository;
    }

    @Transactional
    public boolean deleteUser(String username){
        long deleted = repository.deleteByUsername(username);
        return deleted > 0;
    }

}
