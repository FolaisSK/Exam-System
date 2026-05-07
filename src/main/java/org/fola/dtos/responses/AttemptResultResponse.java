package org.fola.dtos.responses;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class AttemptResultResponse {
    private String id;
    private String examId;
    private String studentId;
    private String status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double score;
    private Double totalPoints;
    private Double percentage;
    private List<AnswerBreakdown> breakdown;

    @Data
    @Builder
    public static class AnswerBreakdown {
        private String questionId;
        private String questionText;
        private String selectedOptionId;
        private String correctOptionId;
        private int pointValue;
        private double earned;
        private boolean correct;
    }
}
