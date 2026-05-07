package org.fola.dtos.responses;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ExamAttemptResponse {
    private String id;
    private String examId;
    private String studentId;
    private String status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double score;
    private Double totalPoints;
}
