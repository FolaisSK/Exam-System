package org.fola.controllers;

import jakarta.validation.Valid;
import org.fola.data.models.User;
import org.fola.dtos.requests.EnrollStudentRequest;
import org.fola.dtos.requests.JoinExamRequest;
import org.fola.dtos.responses.EnrollmentResponse;
import org.fola.services.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {
    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping("/join")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<EnrollmentResponse> joinByCode(
            @Valid @RequestBody JoinExamRequest request,
            @AuthenticationPrincipal User student) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(enrollmentService.joinByCode(request, student));
    }

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<EnrollmentResponse> enrollStudent(
            @Valid @RequestBody EnrollStudentRequest request,
            @AuthenticationPrincipal User teacher) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(enrollmentService.enrollStudent(request, teacher));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<EnrollmentResponse>> getMyEnrollments(
            @AuthenticationPrincipal User student) {
        return ResponseEntity.ok(enrollmentService.getMyEnrollments(student));
    }

    @GetMapping("/exam/{examId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<List<EnrollmentResponse>> getEnrollmentsForExam(
            @PathVariable String examId,
            @AuthenticationPrincipal User teacher) {
        return ResponseEntity.ok(
                enrollmentService.getEnrollmentsForExam(examId, teacher));
    }

}
