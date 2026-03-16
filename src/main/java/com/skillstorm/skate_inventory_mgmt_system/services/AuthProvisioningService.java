package com.skillstorm.skate_inventory_mgmt_system.services;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skillstorm.skate_inventory_mgmt_system.models.OAuthIdentity;
import com.skillstorm.skate_inventory_mgmt_system.models.Role;
import com.skillstorm.skate_inventory_mgmt_system.models.User;
import com.skillstorm.skate_inventory_mgmt_system.repositories.OAuthIdentityRepository;
import com.skillstorm.skate_inventory_mgmt_system.repositories.RoleRepository;
import com.skillstorm.skate_inventory_mgmt_system.repositories.UserRepository;

@Service
public class AuthProvisioningService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final OAuthIdentityRepository oAuthIdentityRepository;

    public AuthProvisioningService(UserRepository userRepository, RoleRepository roleRepository,
            OAuthIdentityRepository oAuthIdentityRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.oAuthIdentityRepository = oAuthIdentityRepository;
    }

    @Transactional
    public User provisionUser(String provider, String providerUserId, String email, String firstName, String lastName) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("OAuth provider did not return an email address.");
        }

        String normalizedProvider = provider == null ? "google" : provider.trim().toLowerCase();
        String normalizedProviderUserId = providerUserId == null ? "" : providerUserId.trim();

        User user = oAuthIdentityRepository.findByProviderAndProviderUserId(normalizedProvider, normalizedProviderUserId)
                .map(OAuthIdentity::getUser)
                .orElseGet(() -> userRepository.findByEmailIgnoreCase(email.trim().toLowerCase()).orElse(null));

        if (user == null) {
            user = new User(firstName, lastName, email);
        } else {
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
        }

        Role guestRole = roleRepository.findByName("GUEST").orElseGet(() -> roleRepository.save(new Role("GUEST")));
        if (user.getRoles().isEmpty()) {
            user.addRole(guestRole);
        }

        user = userRepository.save(user);

        if (!normalizedProviderUserId.isBlank()
                && oAuthIdentityRepository.findByProviderAndProviderUserId(normalizedProvider, normalizedProviderUserId)
                        .isEmpty()) {
            oAuthIdentityRepository.save(new OAuthIdentity(user, normalizedProvider, normalizedProviderUserId));
        }

        return userRepository.findByUserId(user.getUserId()).orElse(user);
    }

    public Collection<? extends GrantedAuthority> toAuthorities(User user,
            Collection<? extends GrantedAuthority> upstreamAuthorities) {
        Set<GrantedAuthority> authorities = new LinkedHashSet<>();
        if (upstreamAuthorities != null) {
            authorities.addAll(upstreamAuthorities);
        }
        user.getRoles().stream()
                .map(Role::getName)
                .map(name -> name.startsWith("ROLE_") ? name : "ROLE_" + name)
                .map(SimpleGrantedAuthority::new)
                .forEach(authorities::add);
        return authorities;
    }

    public Map<String, Object> enrichAttributes(Map<String, Object> attributes, User user) {
        attributes.put("localUserId", user.getUserId());
        attributes.put("dbRoles", user.getRoles().stream().map(Role::getName).sorted().toList());
        return attributes;
    }
}
