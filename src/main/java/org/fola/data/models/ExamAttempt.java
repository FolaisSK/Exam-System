package org.fola.data.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "exam_attempts")
public class ExamAttempt {
    @Id
    private String id;
    private String studentId;
    private String examId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double score;
    private Double totalPoints;
    private AttemptStatus status;
}
