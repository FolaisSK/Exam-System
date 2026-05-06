package org.fola.data.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "question_banks")
public class QuestionBank {
    @Id
    private String id;
    private String ownerId;
    private String name;
    private LocalDateTime createdAt;
}
