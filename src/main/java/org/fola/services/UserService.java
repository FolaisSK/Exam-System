package org.fola.services;

import org.fola.data.models.Role;
import org.fola.data.models.User;
import org.fola.data.repositories.UserRepository;
import org.fola.dtos.responses.UserResponse;
import org.fola.exceptions.ForbiddenException;
import org.fola.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "No user found with email: " + email));
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public UserResponse getUserById(String userId) {
        User user = findUserById(userId);
        return toResponse(user);
    }

    public UserResponse updateRole(String userId, Role newRole) {
        User user = findUserById(userId);
        user.setRole(newRole);
        return toResponse(userRepository.save(user));
    }

    public UserResponse deactivateUser(String userId) {
        User user = findUserById(userId);

        if (!user.isActive()) {
            throw new ForbiddenException("User is already deactivated");
        }

        user.setActive(false);
        return toResponse(userRepository.save(user));
    }

    public UserResponse activateUser(String userId) {
        User user = findUserById(userId);

        if (user.isActive()) {
            throw new ForbiddenException("User is already active");
        }

        user.setActive(true);
        return toResponse(userRepository.save(user));
    }

    public User findUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));
    }

    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .active(user.isActive())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
