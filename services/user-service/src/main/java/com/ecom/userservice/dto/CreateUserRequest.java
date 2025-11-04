package com.ecom.userservice.dto;
import java.util.Set;

// DTOs
public record CreateUserRequest(String username, String email, String password, Set<String> roles) {}