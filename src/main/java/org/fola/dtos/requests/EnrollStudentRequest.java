package org.fola.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EnrollStudentRequest {
    @NotBlank(message = "Student ID is required")
    private String studentId;

    @NotBlank(message = "Exam ID is required")
    private String examId;
}
