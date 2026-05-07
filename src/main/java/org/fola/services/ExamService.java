package org.fola.services;

import org.fola.data.models.Exam;
import org.fola.data.models.ExamQuestion;
import org.fola.data.models.User;
import org.fola.data.repositories.ExamQuestionRepository;
import org.fola.data.repositories.ExamRepository;
import org.fola.dtos.requests.AddExamQuestionRequest;
import org.fola.dtos.requests.CreateExamRequest;
import org.fola.dtos.requests.UpdateExamRequest;
import org.fola.dtos.responses.ExamQuestionResponse;
import org.fola.dtos.responses.ExamResponse;
import org.fola.exceptions.BadRequestException;
import org.fola.exceptions.DuplicateException;
import org.fola.exceptions.ForbiddenException;
import org.fola.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ExamService {
    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private ExamQuestionRepository examQuestionRepository;
    @Autowired
    private QuestionBankService questionBankService;

    public ExamResponse createExam(CreateExamRequest request, User teacher) {
        if (request.isTimed() && (request.getDurationMinutes() == null
                || request.getDurationMinutes() <= 0)) {
            throw new BadRequestException(
                    "Duration is required when exam is timed");
        }

        Exam exam = Exam.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .isTimed(request.isTimed())
                .durationMinutes(request.getDurationMinutes())
                .isPublished(false)
                .accessCode(null)
                .createdBy(teacher.getId())
                .createdAt(LocalDateTime.now())
                .build();

        return toResponse(examRepository.save(exam));
    }

    public List<ExamResponse> getMyExams(User teacher) {
        return examRepository.findAllByCreatedBy(teacher.getId())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ExamResponse getExamById(String examId, User user) {
        Exam exam = findExamById(examId);

        boolean isOwner = exam.getCreatedBy().equals(user.getId());
        boolean isAdmin = user.getRole().name().equals("ADMIN");

        if (!exam.isPublished() && !isOwner && !isAdmin) {
            throw new ResourceNotFoundException(
                    "Exam not found or not available");
        }

        return toResponse(exam);
    }

    public Exam findByAccessCode(String accessCode) {
        return examRepository.findByAccessCode(accessCode)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No published exam found with that access code"));
    }

    public ExamResponse updateExam(String examId,
                                   UpdateExamRequest request,
                                   User teacher) {
        Exam exam = findExamById(examId);
        checkOwnership(exam, teacher);

        if (request.getTitle() != null) exam.setTitle(request.getTitle());
        if (request.getDescription() != null) exam.setDescription(request.getDescription());
        if (request.getIsTimed() != null) exam.setTimed(request.getIsTimed());
        if (request.getDurationMinutes() != null) exam.setDurationMinutes(request.getDurationMinutes());

        if (exam.isTimed() && (exam.getDurationMinutes() == null
                || exam.getDurationMinutes() <= 0)) {
            throw new BadRequestException(
                    "Duration is required when exam is timed");
        }

        return toResponse(examRepository.save(exam));
    }

    public ExamResponse publishExam(String examId, User teacher) {
        Exam exam = findExamById(examId);
        checkOwnership(exam, teacher);

        if (exam.isPublished()) {
            throw new DuplicateException("Exam is already published");
        }

        if (examQuestionRepository.countByExamId(examId) == 0) {
            throw new BadRequestException(
                    "Cannot publish an exam with no questions");
        }

        exam.setPublished(true);
        exam.setAccessCode(generateAccessCode());
        exam.setPublishedAt(LocalDateTime.now());

        return toResponse(examRepository.save(exam));
    }

    public ExamResponse unpublishExam(String examId, User teacher) {
        Exam exam = findExamById(examId);
        checkOwnership(exam, teacher);

        if (!exam.isPublished()) {
            throw new BadRequestException("Exam is not published");
        }

        exam.setPublished(false);
        exam.setAccessCode(null);
        exam.setPublishedAt(null);

        return toResponse(examRepository.save(exam));
    }

    public ExamQuestionResponse addQuestionToExam(String examId,
                                                  AddExamQuestionRequest request,
                                                  User teacher) {
        Exam exam = findExamById(examId);
        checkOwnership(exam, teacher);

        questionBankService.findQuestionById(request.getQuestionId());

        if (examQuestionRepository.existsByExamIdAndQuestionId(
                examId, request.getQuestionId())) {
            throw new DuplicateException(
                    "This question is already added to the exam");
        }

        ExamQuestion examQuestion = ExamQuestion.builder()
                .examId(examId)
                .questionId(request.getQuestionId())
                .orderIndex(request.getOrderIndex())
                .build();

        ExamQuestion saved = examQuestionRepository.save(examQuestion);

        return toExamQuestionResponse(saved, teacher, true);
    }

    public List<ExamQuestionResponse> getExamQuestions(String examId, User user) {
        Exam exam = findExamById(examId);

        boolean isOwner = exam.getCreatedBy().equals(user.getId());
        boolean isAdmin = user.getRole().name().equals("ADMIN");
        boolean includeCorrect = isOwner || isAdmin;

        if (!exam.isPublished() && !isOwner && !isAdmin) {
            throw new ResourceNotFoundException(
                    "Exam not found or not available");
        }

        return examQuestionRepository
                .findAllByExamIdOrderByOrderIndex(examId)
                .stream()
                .map(eq -> toExamQuestionResponse(eq, user, includeCorrect))
                .collect(Collectors.toList());
    }

    public void removeQuestionFromExam(String examId,
                                       String questionId,
                                       User teacher) {
        Exam exam = findExamById(examId);
        checkOwnership(exam, teacher);

        if (!examQuestionRepository.existsByExamIdAndQuestionId(examId, questionId)) {
            throw new ResourceNotFoundException(
                    "Question not found in this exam");
        }

        examQuestionRepository.deleteByExamIdAndQuestionId(examId, questionId);
    }

    public List<ExamResponse> getPublishedExams() {
        return examRepository.findAllByIsPublishedTrue()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Exam findExamById(String examId) {
        return examRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Exam not found with id: " + examId));
    }

    private void checkOwnership(Exam exam, User teacher) {
        if (!exam.getCreatedBy().equals(teacher.getId())) {
            throw new ForbiddenException(
                    "You do not have permission to modify this exam");
        }
    }

    private String generateAccessCode() {
        return UUID.randomUUID().toString()
                .replace("-", "")
                .substring(0, 8)
                .toUpperCase();
    }

    private ExamQuestionResponse toExamQuestionResponse(ExamQuestion eq,
                                                        User user,
                                                        boolean includeCorrect) {
        return ExamQuestionResponse.builder()
                .id(eq.getId())
                .orderIndex(eq.getOrderIndex())
                .question(questionBankService.toQuestionResponse(
                        questionBankService.findQuestionById(eq.getQuestionId()),
                        includeCorrect))
                .build();
    }

    public ExamResponse toResponse(Exam exam) {
        return ExamResponse.builder()
                .id(exam.getId())
                .title(exam.getTitle())
                .description(exam.getDescription())
                .isTimed(exam.isTimed())
                .durationMinutes(exam.getDurationMinutes())
                .isPublished(exam.isPublished())
                .accessCode(exam.getAccessCode())
                .createdBy(exam.getCreatedBy())
                .createdAt(exam.getCreatedAt())
                .publishedAt(exam.getPublishedAt())
                .build();
    }

}
