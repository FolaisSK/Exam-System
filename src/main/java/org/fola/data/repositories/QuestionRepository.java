package org.fola.data.repositories;

import org.fola.data.models.Question;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface QuestionRepository extends MongoRepository<Question, String> {
    List<Question> findAllByBankId(String bankId);

    boolean existsByIdAndBankId(String id, String bankId);
}
