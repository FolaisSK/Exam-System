package org.fola.dtos.responses;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {
    private String id;
    private String name;
    private String email;
    private String role;
    private boolean active;
    private LocalDateTime createdAt;
}
