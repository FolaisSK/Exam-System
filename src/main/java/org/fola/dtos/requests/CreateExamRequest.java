package org.fola.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateExamRequest {
    @NotBlank(message = "Title is required")
    private String title;
    private String description;
    private boolean isTimed;
    private Integer durationMinutes;
}
