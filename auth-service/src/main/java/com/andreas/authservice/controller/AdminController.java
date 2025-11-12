package com.andreas.authservice.controller;

import com.andreas.authservice.model.PromotionRequest;
import com.andreas.authservice.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/role")
public class AdminController {

    private AdminService adminService;

    public AdminController(AdminService adminService){
        this.adminService = adminService;
    }

    @PostMapping("/promote/user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> promote(@RequestBody PromotionRequest request){
        adminService.promote(request.username());
        return ResponseEntity.ok("User Promoted");
    }

}
