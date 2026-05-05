package org.fola.data.repositories;

import org.fola.data.models.Answer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnswerRepository extends MongoRepository<Answer, String> {
    List<Answer> findAllByAttemptId(String attemptId);

    Optional<Answer> findByAttemptIdAndQuestionId(String attemptId, String questionId);

    boolean existsByAttemptIdAndQuestionId(String attemptId, String questionId);
}
