package org.fola.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OptionRequest {
    @NotBlank(message = "Option text is required")
    private String text;

    private boolean isCorrect;
}
