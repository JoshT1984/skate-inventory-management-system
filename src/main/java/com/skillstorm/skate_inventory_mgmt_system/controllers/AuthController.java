package com.skillstorm.skate_inventory_mgmt_system.controllers;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillstorm.skate_inventory_mgmt_system.repositories.UserRepository;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(String.valueOf(authentication.getPrincipal()))) {
            return ResponseEntity.status(401).body(Map.of("authenticated", false));
        }

        Object principal = authentication.getPrincipal();
        OAuth2User oauth2User = principal instanceof OAuth2User ? (OAuth2User) principal : null;
        Long localUserId = null;
        if (oauth2User != null && oauth2User.getAttributes().get("localUserId") != null) {
            localUserId = Long.valueOf(String.valueOf(oauth2User.getAttributes().get("localUserId")));
        }

        var dbUser = localUserId == null ? null : userRepository.findByUserId(localUserId).orElse(null);
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(Objects::nonNull)
                .sorted()
                .toList();

        String email = dbUser != null ? dbUser.getEmail() : oauth2User == null ? null : oauth2User.getAttribute("email");
        String firstName = dbUser != null ? dbUser.getFirstName() : oauth2User == null ? null : oauth2User.getAttribute("given_name");
        String lastName = dbUser != null ? dbUser.getLastName() : oauth2User == null ? null : oauth2User.getAttribute("family_name");

        return ResponseEntity.ok(Map.of(
                "authenticated", true,
                "localUserId", localUserId,
                "email", email == null ? "" : email,
                "firstName", firstName == null ? "" : firstName,
                "lastName", lastName == null ? "" : lastName,
                "roles", roles,
                "provider", principal instanceof OidcUser ? "google" : "oauth2"));
    }
}
