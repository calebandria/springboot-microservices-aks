package com.ecom.userservice.service;

import com.ecom.userservice.domain.User;
import com.ecom.userservice.dto.CreateUserRequest;
import com.ecom.userservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldCreateUserWithDefaultRole() {
        // Given
        CreateUserRequest req = new CreateUserRequest("alice", "alice@ecom.com", "pass123", null);
        when(passwordEncoder.encode("pass123")).thenReturn("hashed123");
        when(repository.existsByEmail("alice@ecom.com")).thenReturn(false);
        when(repository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        User user = userService.createUser(req);

        // Then
        assertNotNull(user.getId());
        assertEquals("alice", user.getUsername());
        assertTrue(user.getRoles().contains("USER"));
        verify(repository).save(any(User.class));
    }
}