package org.fola.data.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "exam_questions")
public class ExamQuestion {
    @Id
    private String id;
    private String examId;
    private String questionId;
    private int orderIndex;
}
