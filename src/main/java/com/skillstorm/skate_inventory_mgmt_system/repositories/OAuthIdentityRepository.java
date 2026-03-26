package com.skillstorm.skate_inventory_mgmt_system.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.skillstorm.skate_inventory_mgmt_system.models.OAuthIdentity;

public interface OAuthIdentityRepository extends JpaRepository<OAuthIdentity, Long> {

    @Query("""
                select oi
                from OAuthIdentity oi
                join fetch oi.user u
                left join fetch u.roles
                where oi.provider = :provider
                  and oi.providerUserId = :providerUserId
            """)
    Optional<OAuthIdentity> findByProviderAndProviderUserId(
            @Param("provider") String provider,
            @Param("providerUserId") String providerUserId);
}