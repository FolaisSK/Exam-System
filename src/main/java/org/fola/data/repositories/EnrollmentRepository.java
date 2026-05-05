package org.fola.data.repositories;

import org.fola.data.models.Enrollment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends MongoRepository<Enrollment, String> {
    boolean existsByStudentIdAndExamId(String studentId, String examId);

    List<Enrollment> findAllByExamId(String examId);

    List<Enrollment> findAllByStudentId(String studentId);
}
