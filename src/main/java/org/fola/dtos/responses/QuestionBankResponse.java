package org.fola.dtos.responses;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class QuestionBankResponse {
    private String id;
    private String name;
    private String ownerId;
    private LocalDateTime createdAt;
}
