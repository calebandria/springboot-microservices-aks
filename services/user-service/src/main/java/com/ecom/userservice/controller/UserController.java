package com.ecom.userservice.controller;

import com.ecom.userservice.domain.Users;
import com.ecom.userservice.service.UserService;
import com.ecom.userservice.dto.CreateUserRequest;
import com.ecom.userservice.dto.UserResponseDto;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;

    @PostMapping
    public ResponseEntity<Users> createUser(@RequestBody CreateUserRequest request) {
        Users user = userService.createUser(request);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long id) {
        UserResponseDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
}
