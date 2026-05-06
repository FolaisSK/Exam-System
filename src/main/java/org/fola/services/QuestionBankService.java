package org.fola.services;

import org.fola.data.models.Option;
import org.fola.data.models.Question;
import org.fola.data.models.QuestionBank;
import org.fola.data.models.User;
import org.fola.data.repositories.QuestionBankRepository;
import org.fola.data.repositories.QuestionRepository;
import org.fola.dtos.requests.AddQuestionRequest;
import org.fola.dtos.requests.CreateQuestionBankRequest;
import org.fola.dtos.responses.OptionResponse;
import org.fola.dtos.responses.QuestionBankResponse;
import org.fola.dtos.responses.QuestionResponse;
import org.fola.exceptions.BadRequestException;
import org.fola.exceptions.ForbiddenException;
import org.fola.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class QuestionBankService {
    @Autowired
    private QuestionBankRepository questionBankRepository;
    @Autowired
    private QuestionRepository questionRepository;

    public QuestionBankResponse createBank(CreateQuestionBankRequest request, User teacher) {
        QuestionBank bank = QuestionBank.builder()
                .name(request.getName())
                .ownerId(teacher.getId())
                .createdAt(LocalDateTime.now())
                .build();

        return toResponse(questionBankRepository.save(bank));
    }

    public List<QuestionBankResponse> getMyBanks(User teacher) {
        return questionBankRepository.findAllByOwnerId(teacher.getId())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public QuestionResponse addQuestion(String bankId,
                                        AddQuestionRequest request,
                                        User teacher) {
        QuestionBank bank = questionBankRepository.findById(bankId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Question bank not found with id: " + bankId));

        if (!bank.getOwnerId().equals(teacher.getId())) {
            throw new ForbiddenException("You do not own this question bank");
        }

        validateOptions(request);

        List<Option> options = request.getOptions().stream()
                .map(opt -> Option.builder()
                        .id(UUID.randomUUID().toString())
                        .text(opt.getText())
                        .isCorrect(opt.isCorrect())
                        .build())
                .collect(Collectors.toList());

        Question question = Question.builder()
                .text(request.getText())
                .pointValue(request.getPointValue())
                .bankId(bankId)
                .options(options)
                .createdAt(LocalDateTime.now())
                .build();

        return toQuestionResponse(questionRepository.save(question), true);
    }

    public List<QuestionResponse> getQuestionsInBank(String bankId, User teacher) {
        QuestionBank bank = questionBankRepository.findById(bankId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Question bank not found with id: " + bankId));

        if (!bank.getOwnerId().equals(teacher.getId())) {
            throw new ForbiddenException("You do not own this question bank");
        }

        return questionRepository.findAllByBankId(bankId)
                .stream()
                .map(q -> toQuestionResponse(q, true))
                .collect(Collectors.toList());
    }

    private void validateOptions(AddQuestionRequest request) {
        long correctCount = request.getOptions().stream()
                .filter(opt -> opt.isCorrect())
                .count();

        if (correctCount == 0) {
            throw new BadRequestException(
                    "Question must have at least one correct option");
        }
        if (correctCount > 1) {
            throw new BadRequestException(
                    "Question must have exactly one correct option");
        }
    }

    public Question findQuestionById(String questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Question not found with id: " + questionId));
    }

    public QuestionResponse toQuestionResponse(Question question, boolean includeCorrect) {
        List<OptionResponse> options = question.getOptions().stream()
                .map(opt -> OptionResponse.builder()
                        .id(opt.getId())
                        .text(opt.getText())
                        .isCorrect(includeCorrect ? opt.isCorrect() : null)
                        .build())
                .collect(Collectors.toList());

        return QuestionResponse.builder()
                .id(question.getId())
                .text(question.getText())
                .pointValue(question.getPointValue())
                .bankId(question.getBankId())
                .options(options)
                .createdAt(question.getCreatedAt())
                .build();
    }

    private QuestionBankResponse toResponse(QuestionBank bank) {
        return QuestionBankResponse.builder()
                .id(bank.getId())
                .name(bank.getName())
                .ownerId(bank.getOwnerId())
                .createdAt(bank.getCreatedAt())
                .build();
    }
}
