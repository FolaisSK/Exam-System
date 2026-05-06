package org.fola.controllers;

import jakarta.validation.Valid;
import org.fola.data.models.User;
import org.fola.dtos.requests.AddQuestionRequest;
import org.fola.dtos.requests.CreateQuestionBankRequest;
import org.fola.dtos.responses.QuestionBankResponse;
import org.fola.dtos.responses.QuestionResponse;
import org.fola.services.QuestionBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/question-banks")
@PreAuthorize("hasRole('TEACHER')")
public class QuestionBankController {

    @Autowired
    private QuestionBankService questionBankService;

    @PostMapping
    public ResponseEntity<QuestionBankResponse> createBank(
            @Valid @RequestBody CreateQuestionBankRequest request,
            @AuthenticationPrincipal User teacher) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(questionBankService.createBank(request, teacher));
    }

    @GetMapping
    public ResponseEntity<List<QuestionBankResponse>> getMyBanks(
            @AuthenticationPrincipal User teacher) {
        return ResponseEntity.ok(questionBankService.getMyBanks(teacher));
    }

    @PostMapping("/{bankId}/questions")
    public ResponseEntity<QuestionResponse> addQuestion(
            @PathVariable String bankId,
            @Valid @RequestBody AddQuestionRequest request,
            @AuthenticationPrincipal User teacher) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(questionBankService.addQuestion(bankId, request, teacher));
    }

    @GetMapping("/{bankId}/questions")
    public ResponseEntity<List<QuestionResponse>> getQuestions(
            @PathVariable String bankId,
            @AuthenticationPrincipal User teacher) {
        return ResponseEntity.ok(
                questionBankService.getQuestionsInBank(bankId, teacher));
    }

}
