package org.fola.data.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "exams")
public class Exam {
    @Id
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
