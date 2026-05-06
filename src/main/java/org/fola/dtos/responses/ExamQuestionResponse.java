package org.fola.dtos.responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExamQuestionResponse {
    private String id;
    private int orderIndex;
    private QuestionResponse question;
}
