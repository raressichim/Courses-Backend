package com.ing.hubs.controller;

import com.ing.hubs.dto.EnrolmentResponseDto;
import com.ing.hubs.exception.teacher.InvalidGradeException;
import com.ing.hubs.service.GradeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/grades")
@Slf4j
@AllArgsConstructor
public class GradeController {
    private final GradeService gradeService;

    @PutMapping("/me/courses/{courseId}")
    public EnrolmentResponseDto gradeStudent(
            @PathVariable Long courseId,
            @RequestBody Map<String, String> gradeData) {

        double grade;
        long studentUserId;
        try {
            grade = Double.parseDouble(gradeData.get("grade"));
            studentUserId = Long.parseLong(gradeData.get("studentId"));
        } catch (NumberFormatException e) {
            log.error("Invalid format for grade or studentId in gradeData: {}", gradeData, e);
            throw new InvalidGradeException();
        }
        EnrolmentResponseDto response = gradeService.gradeStudent(courseId, studentUserId, grade);
        log.info("Exiting gradeStudent with response: {}", response);
        return response;
    }
}
