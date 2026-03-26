package com.skillstorm.skate_inventory_mgmt_system.services;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skillstorm.skate_inventory_mgmt_system.dtos.AuthUserResponse;
import com.skillstorm.skate_inventory_mgmt_system.mappers.AuthMapper;
import com.skillstorm.skate_inventory_mgmt_system.models.OAuthIdentity;
import com.skillstorm.skate_inventory_mgmt_system.models.Role;
import com.skillstorm.skate_inventory_mgmt_system.models.User;
import com.skillstorm.skate_inventory_mgmt_system.repositories.AuthRepository;
import com.skillstorm.skate_inventory_mgmt_system.repositories.UserRepository;

@Service
public class AuthService {

  private final UserRepository userRepository;
  private final AuthMapper authMapper;
  private final AuthRepository authRepository;

  public AuthService(UserRepository userRepository,
      AuthMapper authMapper,
      AuthRepository authRepository) {
    this.userRepository = userRepository;
    this.authMapper = authMapper;
    this.authRepository = authRepository;
  }

  public Optional<AuthUserResponse> getCurrentUser(Authentication authentication) {
    if (authentication == null || !authentication.isAuthenticated()) {
      return Optional.empty();
    }

    String email = extractEmail(authentication);

    if (email == null || email.isBlank()) {
      return Optional.empty();
    }

    return userRepository.findByEmailIgnoreCase(email)
        .map(user -> authMapper.toAuthUserResponse(user, authentication));
  }

  public AuthUserResponse buildUnauthenticatedResponse() {
    return authMapper.toUnauthenticatedResponse();
  }

  @Transactional
  public User provisionUser(String provider,
      String providerUserId,
      String email,
      String firstName,
      String lastName) {

    Optional<OAuthIdentity> existingIdentity = authRepository.findOAuthIdentity(provider, providerUserId);

    if (existingIdentity.isPresent()) {
      return existingIdentity.get().getUser();
    }

    String normalizedEmail = normalizeEmail(email);

    User user = authRepository.findUserByEmailIgnoreCase(normalizedEmail)
        .orElseGet(() -> {
          User newUser = new User();
          newUser.setEmail(normalizedEmail);
          newUser.setFirstName(normalizeName(firstName, "User"));
          newUser.setLastName(normalizeName(lastName, "User"));

          Role guestRole = authRepository.findOrCreateGuestRole();
          newUser.addRole(guestRole);

          return authRepository.saveUser(newUser);
        });

    OAuthIdentity identity = new OAuthIdentity(user, provider, providerUserId);
    authRepository.saveOAuthIdentity(identity);

    return user;
  }

  public Map<String, Object> enrichAttributes(Map<String, Object> attributes, User user) {
    Map<String, Object> enriched = new HashMap<>();
    if (attributes != null) {
      enriched.putAll(attributes);
    }

    enriched.put("localUserId", user.getUserId());
    enriched.put("email", user.getEmail());
    enriched.put("firstName", user.getFirstName());
    enriched.put("lastName", user.getLastName());
    enriched.put(
        "roles",
        user.getRoles().stream()
            .map(Role::getName)
            .sorted()
            .toList());

    return enriched;
  }

  public Set<GrantedAuthority> toAuthorities(User user,
      Collection<? extends GrantedAuthority> existingAuthorities) {
    Set<GrantedAuthority> authorities = new LinkedHashSet<>();

    if (existingAuthorities != null) {
      authorities.addAll(existingAuthorities);
    }

    if (user != null && user.getRoles() != null) {
      user.getRoles().stream()
          .map(Role::getName)
          .filter(name -> name != null && !name.isBlank())
          .map(SimpleGrantedAuthority::new)
          .forEach(authorities::add);
    }

    return authorities;
  }

  private String extractEmail(Authentication authentication) {
    Object principal = authentication.getPrincipal();

    if (principal instanceof org.springframework.security.oauth2.core.user.OAuth2User oauthUser) {
      Object emailAttr = oauthUser.getAttributes().get("email");
      return emailAttr != null ? emailAttr.toString() : null;
    }

    if (principal instanceof org.springframework.security.core.userdetails.UserDetails userDetails) {
      return userDetails.getUsername();
    }

    return null;
  }

  private String normalizeEmail(String email) {
    if (email == null || email.isBlank()) {
      throw new IllegalArgumentException("Email is required for OAuth provisioning.");
    }
    return email.trim().toLowerCase();
  }

  private String normalizeName(String value, String fallback) {
    if (value == null || value.isBlank()) {
      return fallback;
    }
    return value.trim();
  }
}