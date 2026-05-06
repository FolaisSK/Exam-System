package org.fola.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddExamQuestionRequest {
    @NotBlank(message = "Question ID is required")
    private String questionId;

    private int orderIndex;
}
