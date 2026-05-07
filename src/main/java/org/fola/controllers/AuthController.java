package org.fola.controllers;

import jakarta.validation.Valid;
import org.fola.data.models.User;
import org.fola.dtos.requests.LoginRequest;
import org.fola.dtos.requests.RegisterRequest;
import org.fola.dtos.responses.AuthResponse;
import org.fola.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(Map.of(
                "id",          user.getId(),
                "email",       user.getEmail(),
                "role",        user.getRole(),
                "authorities", user.getAuthorities().toString(),
                "isActive",    user.isActive()
        ));
    }
}
