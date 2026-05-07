package org.fola.services;

import org.fola.data.models.Answer;
import org.fola.data.models.AttemptStatus;
import org.fola.data.models.ExamAttempt;
import org.fola.data.models.User;
import org.fola.data.repositories.AnswerRepository;
import org.fola.data.repositories.ExamAttemptRepository;
import org.fola.data.repositories.ExamQuestionRepository;
import org.fola.dtos.responses.AttemptResultResponse;
import org.fola.dtos.responses.ExamAttemptResponse;
import org.fola.exceptions.ForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ExamAttemptService {
    @Autowired
    private ExamAttemptRepository examAttemptRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private ExamQuestionRepository examQuestionRepository;
    @Autowired
    private ExamService examService;
    @Autowired
    private EnrollmentService enrollmentService;
    @Autowired
    private QuestionBankService questionBankService;

    public ExamAttemptResponse startAttempt(String examId, User student) {
        examService.findExamById(examId);

        if (!enrollmentService.isEnrolled(student.getId(), examId)) {
            throw new ForbiddenException(
                    "You are not enrolled in this exam");
        }

        ExamAttempt attempt = ExamAttempt.builder()
                .studentId(student.getId())
                .examId(examId)
                .startTime(LocalDateTime.now())
                .status(AttemptStatus.IN_PROGRESS)
                .score(null)
                .totalPoints(null)
                .build();

        return toAttemptResponse(examAttemptRepository.save(attempt));
    }

    public AnswerResponse saveAnswer(String attemptId,
                                     SaveAnswerRequest request,
                                     User student) {
        ExamAttempt attempt = findAttemptForStudent(attemptId, student);
        checkAttemptInProgress(attempt);

        validateQuestionBelongsToExam(attempt.getExamId(), request.getQuestionId());
        validateOptionBelongsToQuestion(request.getQuestionId(),
                request.getSelectedOptionId());

        if (answerRepository.existsByAttemptIdAndQuestionId(
                attemptId, request.getQuestionId())) {
            Answer existing = answerRepository
                    .findByAttemptIdAndQuestionId(attemptId, request.getQuestionId())
                    .get();
            existing.setSelectedOptionId(request.getSelectedOptionId());
            existing.setAnsweredAt(LocalDateTime.now());
            return toAnswerResponse(answerRepository.save(existing));
        }

        Answer answer = Answer.builder()
                .attemptId(attemptId)
                .questionId(request.getQuestionId())
                .selectedOptionId(request.getSelectedOptionId())
                .answeredAt(LocalDateTime.now())
                .build();

        return toAnswerResponse(answerRepository.save(answer));
    }

    public AttemptResultResponse submitAttempt(String attemptId, User student) {
        ExamAttempt attempt = findAttemptForStudent(attemptId, student);
        checkAttemptInProgress(attempt);

        List<ExamQuestion> examQuestions = examQuestionRepository
                .findAllByExamIdOrderByOrderIndex(attempt.getExamId());

        List<Answer> answers = answerRepository.findAllByAttemptId(attemptId);

        double totalPoints = 0;
        double earnedScore = 0;
        List<AttemptResultResponse.AnswerBreakdown> breakdown = new ArrayList<>();

        for (ExamQuestion eq : examQuestions) {
            Question question = questionBankService
                    .findQuestionById(eq.getQuestionId());

            totalPoints += question.getPointValue();

            String correctOptionId = question.getOptions().stream()
                    .filter(Option::isCorrect)
                    .map(Option::getId)
                    .findFirst()
                    .orElse(null);

            Answer studentAnswer = answers.stream()
                    .filter(a -> a.getQuestionId().equals(question.getId()))
                    .findFirst()
                    .orElse(null);

            String selectedOptionId = studentAnswer != null
                    ? studentAnswer.getSelectedOptionId() : null;

            boolean isCorrect = selectedOptionId != null
                    && selectedOptionId.equals(correctOptionId);

            double earned = isCorrect ? question.getPointValue() : 0;
            earnedScore += earned;

            breakdown.add(AttemptResultResponse.AnswerBreakdown.builder()
                    .questionId(question.getId())
                    .questionText(question.getText())
                    .selectedOptionId(selectedOptionId)
                    .correctOptionId(correctOptionId)
                    .pointValue(question.getPointValue())
                    .earned(earned)
                    .correct(isCorrect)
                    .build());
        }

        double percentage = totalPoints > 0
                ? Math.round((earnedScore / totalPoints) * 1000.0) / 10.0
                : 0;

        attempt.setScore(earnedScore);
        attempt.setTotalPoints(totalPoints);
        attempt.setStatus(AttemptStatus.SUBMITTED);
        attempt.setEndTime(LocalDateTime.now());
        examAttemptRepository.save(attempt);

        return AttemptResultResponse.builder()
                .id(attempt.getId())
                .examId(attempt.getExamId())
                .studentId(attempt.getStudentId())
                .status(attempt.getStatus().name())
                .startTime(attempt.getStartTime())
                .endTime(attempt.getEndTime())
                .score(earnedScore)
                .totalPoints(totalPoints)
                .percentage(percentage)
                .breakdown(breakdown)
                .build();
    }
}

