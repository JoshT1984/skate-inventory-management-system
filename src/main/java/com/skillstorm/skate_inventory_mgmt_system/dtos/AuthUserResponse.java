package com.skillstorm.skate_inventory_mgmt_system.dtos;

import java.util.List;

public class AuthUserResponse {
    private boolean authenticated;
    private Long localUserId;
    private String email;
    private String firstName;
    private String lastName;
    private List<String> roles;
    private String provider;

    public AuthUserResponse() {
    }

    public AuthUserResponse(boolean authenticated, Long localUserId, String email,
            String firstName, String lastName, List<String> roles,
            String provider) {
        this.authenticated = authenticated;
        this.localUserId = localUserId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = roles;
        this.provider = provider;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public Long getLocalUserId() {
        return localUserId;
    }

    public void setLocalUserId(Long localUserId) {
        this.localUserId = localUserId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}