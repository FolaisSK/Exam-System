package org.fola.controllers;

import org.fola.data.models.Role;
import org.fola.dtos.responses.UserResponse;
import org.fola.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable String userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PutMapping("/users/{userId}/role")
    public ResponseEntity<UserResponse> updateRole(@PathVariable String userId,
                                                   @RequestParam Role role) {
        return ResponseEntity.ok(userService.updateRole(userId, role));
    }

    @PutMapping("/users/{userId}/deactivate")
    public ResponseEntity<UserResponse> deactivate(@PathVariable String userId) {
        return ResponseEntity.ok(userService.deactivateUser(userId));
    }

    @PutMapping("/users/{userId}/activate")
    public ResponseEntity<UserResponse> activate(@PathVariable String userId) {
        return ResponseEntity.ok(userService.activateUser(userId));
    }
}
