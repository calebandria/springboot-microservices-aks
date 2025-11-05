package com.ecom.userservice.service;

import com.ecom.userservice.domain.Role;
import com.ecom.userservice.domain.Users;
import com.ecom.userservice.repository.UserRepository;
import com.ecom.userservice.dto.CreateUserRequest;
import com.ecom.userservice.dto.UserResponseDetails;
import com.ecom.userservice.dto.UserResponseDto;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    @Autowired
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public Users createUser(CreateUserRequest request) {
        if (repository.findByEmail(request.email()).isPresent()) {
            throw new IllegalStateException("Email " + request.email() + " is already taken.");
        }

        // 2. Data Mapping and Transformation
        Users user = new Users();
        user.setUsername(request.username());
        user.setEmail(request.email());

        // SECURE: Hash the plain password using the injected PasswordEncoder
        String hashedPassword = passwordEncoder.encode(request.password());
        user.setPasswordHash(hashedPassword);

        // Map the roles. If the set is null or empty, default the user to BUYER role.
        Set<Role> assignedRoles = request.roles() != null && !request.roles().isEmpty()
                ? request.roles().stream()
                        // Convert each role string (e.g., "BUYER") to the Role enum (Role.BUYER)
                        .map((String roleString) -> {
                            try {
                                return Role.valueOf(roleString.toUpperCase());
                            } catch (IllegalArgumentException e) {
                                // If an invalid role string is sent (e.g., "GUEST"), stop creation
                                throw new IllegalArgumentException("Invalid role provided: " + roleString);
                            }
                        })
                        .collect(Collectors.toSet())
                : Set.of(Role.BUYER);

        user.setRoles(assignedRoles);

        return repository.save(user);
    }

    public UserResponseDto getUserById(Long id) {
        return mapUserToResponDto(repository.getById((id)));
    }

    private UserResponseDto mapUserToResponDto(Users user) {
        UserResponseDto userResonseDto = new UserResponseDto(
                user.getUsername(),
                user.getEmail(),
                user.getRoles().stream()
                        .map((Function<? super Role, ? extends String>) userRoles -> {
                            return userRoles.toString();
                        })
                        .collect(Collectors.toSet()));

        return userResonseDto;
    }

    private UserResponseDetails mapuserToResponseDetails(Users user) {
        UserResponseDetails userDetails = new UserResponseDetails(
                user.getUsername(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getRoles().stream()
                        .map((Function<? super Role, ? extends String>) userRoles -> {
                            return userRoles.toString();
                        })
                        .collect(Collectors.toSet()));
        return userDetails;
    }

    public UserResponseDetails getUserByEmail(String email) {
        return mapuserToResponseDetails(repository.getByEmail((email)));
    }

}

// Exceptions
class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}

class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String message) {
        super(message);
    }
}