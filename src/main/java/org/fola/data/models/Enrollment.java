package org.fola.data.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "enrollments")
public class Enrollment {
    @Id
    private String id;
    private String studentId;
    private String examId;
    private EnrollmentMethod method;
    private LocalDateTime enrolledAt;
}
