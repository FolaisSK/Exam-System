package org.fola.data.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Document(collection = "questions")
public class Question {
    @Id
    private String id;
    private String text;
    private int pointValue;
    private String bankId;
    private List<Option> options;
    private LocalDateTime createdAt;
}
