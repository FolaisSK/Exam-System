package org.fola.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateQuestionBankRequest {
    @NotBlank(message = "Bank name is required")
    private String name;
}
