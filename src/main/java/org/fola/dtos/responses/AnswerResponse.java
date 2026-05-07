package org.fola.dtos.responses;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AnswerResponse {
    private String id;
    private String attemptId;
    private String questionId;
    private String selectedOptionId;
    private LocalDateTime answeredAt;
}
