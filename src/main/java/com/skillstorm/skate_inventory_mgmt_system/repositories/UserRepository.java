package com.skillstorm.skate_inventory_mgmt_system.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.skillstorm.skate_inventory_mgmt_system.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = { "roles" })
    Optional<User> findByEmailIgnoreCase(String email);

    @EntityGraph(attributePaths = { "roles" })
    Optional<User> findByUserId(Long userId);
}
