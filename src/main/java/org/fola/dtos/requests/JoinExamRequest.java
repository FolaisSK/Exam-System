package org.fola.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class JoinExamRequest {
    @NotBlank(message = "Access code is required")
    private String accessCode;
}
