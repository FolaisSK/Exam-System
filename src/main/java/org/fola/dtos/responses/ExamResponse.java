package org.fola.dtos.responses;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ExamResponse {
    private String id;
    private String title;
    private String description;
    private boolean isTimed;
    private Integer durationMinutes;
    private boolean isPublished;
    private String accessCode;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime publishedAt;
}
