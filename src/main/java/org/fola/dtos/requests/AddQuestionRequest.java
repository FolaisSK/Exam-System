package org.fola.dtos.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class AddQuestionRequest {
    @NotBlank(message = "Question text is required")
    private String text;

    @Min(value = 1, message = "Point value must be at least 1")
    private int pointValue;

    @NotEmpty(message = "Options are required")
    @Size(min = 2, max = 6, message = "A question must have between 2 and 6 options")
    @Valid
    private List<OptionRequest> options;
}
