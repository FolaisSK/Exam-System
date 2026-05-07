package org.fola.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OptionRequest {
    @NotBlank(message = "Option text is required")
    private String text;
    @JsonProperty("isCorrect")
    private boolean correct;
}
