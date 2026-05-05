package org.fola.data.repositories;

import org.fola.data.models.Exam;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamRepository extends MongoRepository<Exam, String> {
    Optional<Exam> findByAccessCode(String accessCode);

    List<Exam> findAllByCreatedBy(String teacherId);

    List<Exam> findAllByIsPublishedTrue();

    boolean existsByIdAndCreatedBy(String id, String createdBy);
}
