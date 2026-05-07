package org.fola.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SaveAnswerRequest {
    @NotBlank(message = "Question ID is required")
    private String questionId;

    @NotBlank(message = "Selected option ID is required")
    private String selectedOptionId;
}
