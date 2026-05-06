package org.fola.dtos.responses;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class QuestionResponse {
    private String id;
    private String text;
    private int pointValue;
    private String bankId;
    private List<OptionResponse> options;
    private LocalDateTime createdAt;
}
