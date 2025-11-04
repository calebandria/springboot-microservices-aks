package com.ecom.userservice.dto;

import java.util.Set;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserResponseDto {
    String username;
    String email;
    Set<String> roles;
}
