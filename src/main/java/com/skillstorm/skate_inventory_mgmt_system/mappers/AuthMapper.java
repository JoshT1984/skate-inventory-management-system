package com.skillstorm.skate_inventory_mgmt_system.mappers;

import java.util.List;
import java.util.Objects;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

import com.skillstorm.skate_inventory_mgmt_system.dtos.AuthUserResponse;
import com.skillstorm.skate_inventory_mgmt_system.models.User;

@Component
public class AuthMapper {

    public AuthUserResponse toAuthUserResponse(User user, Authentication authentication) {
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(Objects::nonNull)
                .sorted()
                .toList();

        String provider = authentication.getPrincipal() instanceof OidcUser ? "google" : "oauth2";

        return new AuthUserResponse(
                true,
                user.getUserId(),
                user.getEmail() == null ? "" : user.getEmail(),
                user.getFirstName() == null ? "" : user.getFirstName().trim(),
                user.getLastName() == null ? "" : user.getLastName().trim(),
                roles,
                provider);
    }

    public AuthUserResponse toUnauthenticatedResponse() {
        return new AuthUserResponse(
                false,
                null,
                "",
                "",
                "",
                List.of(),
                "");
    }
}