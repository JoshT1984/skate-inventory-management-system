package com.skillstorm.skate_inventory_mgmt_system.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skillstorm.skate_inventory_mgmt_system.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserId(Long userId);

    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = "roles")
    Optional<User> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);
}