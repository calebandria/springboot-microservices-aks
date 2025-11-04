package com.ecom.userservice.controller;

import com.ecom.userservice.config.TestSecurityConfig;
import com.ecom.userservice.domain.Role;
import com.ecom.userservice.domain.Users;
import com.ecom.userservice.dto.CreateUserRequest;
import com.ecom.userservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(TestSecurityConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnUserNotFound() throws Exception {
        // Given
        Long id = (long) 12578;
        when(userService.getUserById(id)).thenThrow(new UsernameNotFoundException("User not found: " + id));

        // When & Then

        mockMvc.perform(get("/users/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateUser() throws Exception {
        // Given
        CreateUserRequest req = new CreateUserRequest("bob", "bob@ecom.com", "pass", Set.of("SELLER"));
        Users savedUser = Users.builder()
                .username(req.username())
                .email(req.email())
                .roles(req.roles().stream().map((String role) ->{
                    return Role.valueOf(role);
                }
                ).collect(Collectors.toSet()))
                .build();

        when(userService.createUser(req)).thenReturn(savedUser);

        // When & Then
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("bob"))
                .andExpect(jsonPath("$.roles[0]").value("SELLER"));
    }
}