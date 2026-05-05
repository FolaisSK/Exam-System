package org.fola.data.repositories;

import org.fola.data.models.AttemptStatus;
import org.fola.data.models.ExamAttempt;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamAttemptRepository extends MongoRepository<ExamAttempt, String> {
    List<ExamAttempt> findAllByStudentIdAndExamId(String studentId, String examId);

    List<ExamAttempt> findAllByExamId(String examId);

    Optional<ExamAttempt> findByIdAndStudentId(String id, String studentId);

    boolean existsByIdAndStatus(String id, AttemptStatus status);
}
