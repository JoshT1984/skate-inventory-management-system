package com.skillstorm.skate_inventory_mgmt_system.repositories;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.skillstorm.skate_inventory_mgmt_system.models.OAuthIdentity;
import com.skillstorm.skate_inventory_mgmt_system.models.Role;
import com.skillstorm.skate_inventory_mgmt_system.models.User;

@Repository
public class AuthRepository {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final OAuthIdentityRepository oAuthIdentityRepository;

    public AuthRepository(UserRepository userRepository,
            RoleRepository roleRepository,
            OAuthIdentityRepository oAuthIdentityRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.oAuthIdentityRepository = oAuthIdentityRepository;
    }

    public Optional<User> findUserById(Long userId) {
        return userRepository.findByUserId(userId);
    }

    public Optional<User> findUserByEmailIgnoreCase(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }

    public Optional<OAuthIdentity> findOAuthIdentity(String provider, String providerUserId) {
        return oAuthIdentityRepository.findByProviderAndProviderUserId(provider, providerUserId);
    }

    public Role findOrCreateGuestRole() {
        return roleRepository.findByName("ROLE_GUEST")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_GUEST")));
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public OAuthIdentity saveOAuthIdentity(OAuthIdentity identity) {
        return oAuthIdentityRepository.save(identity);
    }
}