package com.andreas.authservice.controller;

import com.andreas.authservice.model.PromotionRequest;
import com.andreas.authservice.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/admin")
public class AdminController {

    private AdminService adminService;

    public AdminController(AdminService adminService){
        this.adminService = adminService;
    }

    @DeleteMapping("/delete/user/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable String username){
        boolean deleted = adminService.deleteUser(username);
        if(deleted){
           return ResponseEntity.ok("User " + username + " was deleted");
        }
        return ResponseEntity.notFound().build();
    }

}
