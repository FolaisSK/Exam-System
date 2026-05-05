package org.fola.data.repositories;

import org.fola.data.models.ExamQuestion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamQuestionRepository extends MongoRepository<ExamQuestion, String> {
    List<ExamQuestion> findAllByExamIdOrderByOrderIndex(String examId);

    boolean existsByExamIdAndQuestionId(String examId, String questionId);

    void deleteByExamIdAndQuestionId(String examId, String questionId);

    int countByExamId(String examId);
}
