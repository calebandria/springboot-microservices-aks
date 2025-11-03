package com.ecom.userservice.controller;

import com.ecom.userservice.domain.User;
import com.ecom.userservice.service.UserService;
import com.ecom.userservice.dto.CreateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest request) {
        User user = userService.createUser(request);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable String id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<Set<User>> getUsersByRole(@PathVariable String role) {
        Set<User> users = userService.getUsersByRole(role);
        return ResponseEntity.ok(users);
    }
}
