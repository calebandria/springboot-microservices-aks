package com.ecom.userservice.dto;

import java.util.Set;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserResponseDetails {
    String username;
    String email;
    String hashedPassword;
    Set<String> roles;
}
