package org.fola.services;

import jakarta.validation.Valid;
import org.fola.data.models.*;
import org.fola.data.repositories.EnrollmentRepository;
import org.fola.dtos.requests.EnrollStudentRequest;
import org.fola.dtos.requests.JoinExamRequest;
import org.fola.dtos.responses.EnrollmentResponse;
import org.fola.exceptions.BadRequestException;
import org.fola.exceptions.DuplicateException;
import org.fola.exceptions.ForbiddenException;
import org.fola.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private ExamService examService;
    @Autowired
    private UserService userService;

    public EnrollmentResponse joinByCode(JoinExamRequest request, User student) {
        if (!student.getRole().equals(Role.STUDENT)) {
            throw new ForbiddenException("Only students can enroll in exams");
        }

        Exam exam = examService.findByAccessCode(request.getAccessCode());

        if (!exam.isPublished()) {
            throw new ResourceNotFoundException(
                    "No published exam found with that access code");
        }

        if (enrollmentRepository.existsByStudentIdAndExamId(
                student.getId(), exam.getId())) {
            throw new DuplicateException(
                    "You are already enrolled in this exam");
        }

        Enrollment enrollment = Enrollment.builder()
                .studentId(student.getId())
                .examId(exam.getId())
                .method(EnrollmentMethod.CODE)
                .enrolledAt(LocalDateTime.now())
                .build();

        return toResponse(enrollmentRepository.save(enrollment));
    }

    public EnrollmentResponse enrollStudent(@Valid EnrollStudentRequest request,
                                            User teacher) {
        Exam exam = examService.findExamById(request.getExamId());

        if (!exam.getCreatedBy().equals(teacher.getId())) {
            throw new ForbiddenException(
                    "You can only enroll students in your own exams");
        }

        User student = userService.findUserById(request.getStudentId());

        if (!student.getRole().equals(Role.STUDENT)) {
            throw new BadRequestException("User is not a student");
        }

        if (enrollmentRepository.existsByStudentIdAndExamId(
                request.getStudentId(), request.getExamId())) {
            throw new DuplicateException(
                    "Student is already enrolled in this exam");
        }

        Enrollment enrollment = Enrollment.builder()
                .studentId(request.getStudentId())
                .examId(request.getExamId())
                .method(EnrollmentMethod.ENROLLED)
                .enrolledAt(LocalDateTime.now())
                .build();

        return toResponse(enrollmentRepository.save(enrollment));
    }

    public List<EnrollmentResponse> getEnrollmentsForExam(String examId,
                                                          User teacher) {
        Exam exam = examService.findExamById(examId);

        if (!exam.getCreatedBy().equals(teacher.getId())) {
            throw new ForbiddenException(
                    "You can only view enrollments for your own exams");
        }

        return enrollmentRepository.findAllByExamId(examId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<EnrollmentResponse> getMyEnrollments(User student) {
        return enrollmentRepository.findAllByStudentId(student.getId())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public boolean isEnrolled(String studentId, String examId) {
        return enrollmentRepository.existsByStudentIdAndExamId(studentId, examId);
    }

    private EnrollmentResponse toResponse(Enrollment enrollment) {
        return EnrollmentResponse.builder()
                .id(enrollment.getId())
                .studentId(enrollment.getStudentId())
                .examId(enrollment.getExamId())
                .method(enrollment.getMethod().name())
                .enrolledAt(enrollment.getEnrolledAt())
                .build();
    }
}
