package com.skillstorm.skate_inventory_mgmt_system.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillstorm.skate_inventory_mgmt_system.dtos.AuthUserResponse;
import com.skillstorm.skate_inventory_mgmt_system.services.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/me")
    public ResponseEntity<AuthUserResponse> me(Authentication authentication) {
        return authService.getCurrentUser(authentication)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(401).body(authService.buildUnauthenticatedResponse()));
    }
}