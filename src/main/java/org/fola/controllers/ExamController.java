package org.fola.controllers;

import jakarta.validation.Valid;
import org.fola.data.models.User;
import org.fola.dtos.requests.AddExamQuestionRequest;
import org.fola.dtos.requests.CreateExamRequest;
import org.fola.dtos.requests.UpdateExamRequest;
import org.fola.dtos.responses.ExamQuestionResponse;
import org.fola.dtos.responses.ExamResponse;
import org.fola.services.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
public class ExamController {
    @Autowired
    private ExamService examService;

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ExamResponse> createExam(
            @Valid @RequestBody CreateExamRequest request,
            @AuthenticationPrincipal User teacher) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(examService.createExam(request, teacher));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<List<ExamResponse>> getMyExams(
            @AuthenticationPrincipal User teacher) {
        return ResponseEntity.ok(examService.getMyExams(teacher));
    }

    @GetMapping("/published")
    public ResponseEntity<List<ExamResponse>> getPublishedExams() {
        return ResponseEntity.ok(examService.getPublishedExams());
    }

    @GetMapping("/{examId}")
    public ResponseEntity<ExamResponse> getExam(
            @PathVariable String examId,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(examService.getExamById(examId, user));
    }

    @PutMapping("/{examId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ExamResponse> updateExam(
            @PathVariable String examId,
            @RequestBody UpdateExamRequest request,
            @AuthenticationPrincipal User teacher) {
        return ResponseEntity.ok(examService.updateExam(examId, request, teacher));
    }

    @PutMapping("/{examId}/publish")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ExamResponse> publishExam(
            @PathVariable String examId,
            @AuthenticationPrincipal User teacher) {
        return ResponseEntity.ok(examService.publishExam(examId, teacher));
    }

    @PutMapping("/{examId}/unpublish")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ExamResponse> unpublishExam(
            @PathVariable String examId,
            @AuthenticationPrincipal User teacher) {
        return ResponseEntity.ok(examService.unpublishExam(examId, teacher));
    }

    @PostMapping("/{examId}/questions")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ExamQuestionResponse> addQuestion(
            @PathVariable String examId,
            @Valid @RequestBody AddExamQuestionRequest request,
            @AuthenticationPrincipal User teacher) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(examService.addQuestionToExam(examId, request, teacher));
    }

    @GetMapping("/{examId}/questions")
    public ResponseEntity<List<ExamQuestionResponse>> getQuestions(
            @PathVariable String examId,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(examService.getExamQuestions(examId, user));
    }

    @DeleteMapping("/{examId}/questions/{questionId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Void> removeQuestion(
            @PathVariable String examId,
            @PathVariable String questionId,
            @AuthenticationPrincipal User teacher) {
        examService.removeQuestionFromExam(examId, questionId, teacher);
        return ResponseEntity.noContent().build();
    }
}
