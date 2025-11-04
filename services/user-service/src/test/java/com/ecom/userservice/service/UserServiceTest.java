package com.ecom.userservice.service;

import com.ecom.userservice.domain.Role; 
import com.ecom.userservice.domain.Users;
import com.ecom.userservice.dto.CreateUserRequest;
import com.ecom.userservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
        CreateUserRequest req = new CreateUserRequest("alice", "alice@ecom.com", "pass123", null);
        
        Users userAfterSave = Users.builder()
                .id(1L) 
                .username("alice")
                .email("alice@ecom.com")
                .roles(Set.of(Role.BUYER))
                .build();

        when(passwordEncoder.encode("pass123")).thenReturn("hashed123");
        when(repository.save(any(Users.class))).thenReturn(userAfterSave); 

        Users resultUser = userService.createUser(req); 
        verify(repository).save(any(Users.class)); 
        
        assertNotNull(resultUser, "The created user object should not be null.");
        assertNotNull(resultUser.getId(), "The user ID should be assigned by the mock repository.");
        assertEquals("alice", resultUser.getUsername());
        assertTrue(resultUser.getRoles().contains(Role.valueOf("BUYER")), "User should have the default 'BUYER' role.");
    }
}