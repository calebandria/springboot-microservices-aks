package com.ecom.userservice.service;

import com.ecom.userservice.domain.User;
import com.ecom.userservice.repository.UserRepository;
import com.ecom.userservice.dto.CreateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(CreateUserRequest request) {
        validateRequest(request);
        
        User user = User.builder()
            .id(UUID.randomUUID().toString())
            .username(request.username())
            .email(request.email())
            .passwordHash(passwordEncoder.encode(request.password()))
            .roles(request.roles() != null ? request.roles() : Set.of("USER"))
            .build();
            
        return repository.save(user);
    }

    public User getUserById(String id) {
        return repository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User not found: " + id));
    }

    public Set<User> getUsersByRole(String role) {
        validateRole(role);
        return repository.findByRole(role.toUpperCase());
    }

    private void validateRequest(CreateUserRequest request) {
        if (repository.existsByEmail(request.email())) {
            throw new DuplicateEmailException("Email already exists");
        }
        if (request.username() == null || request.username().trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }
    }

    private void validateRole(String role) {
        Set<String> validRoles = Set.of("USER", "VENDOR", "ADMIN");
        if (!validRoles.contains(role.toUpperCase())) {
            throw new IllegalArgumentException("Invalid role: " + role);
        }
    }
}


// Exceptions
class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) { super(message); }
}

class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String message) { super(message); }
}