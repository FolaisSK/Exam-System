package org.fola.data.repositories;

import org.fola.data.models.QuestionBank;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionBankRepository extends MongoRepository<QuestionBank, String> {
    List<QuestionBank> findAllByOwnerId(String ownerId);

    boolean existsByIdAndOwnerId(String id, String ownerId);
}
