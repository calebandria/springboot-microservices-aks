package com.ecom.userservice.domain;

import java.util.Set;

public enum Role {
    
    ADMIN, // System administrators with full privileges
    BUYER, // Standard customer for purchasing products
    SELLER // Vendor/merchant responsible for listing and selling products
;

    Set<Role> valueOf(Set<String> roles) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'valueOf'");
    }
}