package com.skillstorm.skate_inventory_mgmt_system.api;

public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }
}
