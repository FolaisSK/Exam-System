package org.fola.dtos.responses;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EnrollmentResponse {
    private String id;
    private String studentId;
    private String examId;
    private String method;
    private LocalDateTime enrolledAt;
}
