package org.fola.data.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "answers")
public class Answer {
    @Id
    private String id;
    private String attemptId;
    private String questionId;
    private String selectedOptionId;
    private LocalDateTime answeredAt;
}
