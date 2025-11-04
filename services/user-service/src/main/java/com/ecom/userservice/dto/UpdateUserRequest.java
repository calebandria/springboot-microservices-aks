package com.ecom.userservice.dto;

import java.util.Optional;
import java.util.Set;

public record UpdateUserRequest(
    Long userId, 
    Optional<String> username,
    Optional<String> email,
    Optional<String> newPassword, 
    Optional<Set<String>> roles 
) {}
