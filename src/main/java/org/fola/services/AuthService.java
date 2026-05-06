package org.fola.services;

import org.fola.data.models.User;
import org.fola.data.repositories.UserRepository;
import org.fola.dtos.requests.LoginRequest;
import org.fola.dtos.requests.RegisterRequest;
import org.fola.dtos.responses.AuthResponse;
import org.fola.exceptions.DuplicateException;
import org.fola.exceptions.ForbiddenException;
import org.fola.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private  PasswordEncoder passwordEncoder;
    @Autowired
    private  JwtUtil jwtUtil;
    @Autowired
    private  AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateException(
                    "An account with email " + request.getEmail() + " already exists");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        User saved = userRepository.save(user);
        String token = jwtUtil.generateToken(saved);

        return buildAuthResponse(saved, token);
    }

    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            throw new ForbiddenException("Invalid email or password");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ForbiddenException("Invalid email or password"));

        if (!user.isActive()) {
            throw new ForbiddenException("This account has been deactivated");
        }

        String token = jwtUtil.generateToken(user);
        return buildAuthResponse(user, token);
    }

    private AuthResponse buildAuthResponse(User user, String token) {
        return AuthResponse.builder()
                .token(token)
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}
