package com.andreas.authservice.service;

import org.springframework.stereotype.Service;

@Service
public class AdminService {

    public void promote(String username){
        System.out.println("I AM AN ADMIN");
    }

}
