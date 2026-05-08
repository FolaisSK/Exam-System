package org.fola.controllers;

import jakarta.validation.Valid;
import org.fola.data.models.User;
import org.fola.dtos.requests.SaveAnswerRequest;
import org.fola.dtos.responses.AnswerResponse;
import org.fola.dtos.responses.AttemptResultResponse;
import org.fola.dtos.responses.ExamAttemptResponse;
import org.fola.services.ExamAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attempts")
public class ExamAttemptController {
    @Autowired
    private ExamAttemptService examAttemptService;

    @PostMapping("/exam/{examId}/start")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ExamAttemptResponse> startAttempt(
            @PathVariable String examId,
            @AuthenticationPrincipal User student) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(examAttemptService.startAttempt(examId, student));
    }

    @PostMapping("/{attemptId}/answers")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<AnswerResponse> saveAnswer(
            @PathVariable String attemptId,
            @Valid @RequestBody SaveAnswerRequest request,
            @AuthenticationPrincipal User student) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(examAttemptService.saveAnswer(attemptId, request, student));
    }

    @PostMapping("/{attemptId}/submit")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<AttemptResultResponse> submitAttempt(
            @PathVariable String attemptId,
            @AuthenticationPrincipal User student) {
        return ResponseEntity.ok(
                examAttemptService.submitAttempt(attemptId, student));
    }

    @GetMapping("/{attemptId}/result")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<AttemptResultResponse> getResult(
            @PathVariable String attemptId,
            @AuthenticationPrincipal User student) {
        return ResponseEntity.ok(
                examAttemptService.getAttemptResult(attemptId, student));
    }

    @GetMapping("/exam/{examId}/my")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<ExamAttemptResponse>> getMyAttempts(
            @PathVariable String examId,
            @AuthenticationPrincipal User student) {
        return ResponseEntity.ok(
                examAttemptService.getMyAttempts(examId, student));
    }

    @GetMapping("/exam/{examId}/all")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<List<ExamAttemptResponse>> getAttemptsForExam(
            @PathVariable String examId,
            @AuthenticationPrincipal User teacher) {
        return ResponseEntity.ok(
                examAttemptService.getAttemptsForExam(examId, teacher));
    }
}
