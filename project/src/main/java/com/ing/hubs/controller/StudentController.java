package com.ing.hubs.controller;

import com.ing.hubs.dto.*;
import com.ing.hubs.service.EnrolmentService;
import com.ing.hubs.service.StudentService;
import com.ing.hubs.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@Slf4j
@AllArgsConstructor
public class StudentController {
    private final UserService userService;
    private final StudentService studentService;

    @GetMapping("/me/grades")
    @PreAuthorize("hasAuthority('STUDENT')")
    public List<EnrolmentStudentResponseDto> getGradesForEnrolledCourses() {
        List<EnrolmentStudentResponseDto> response = studentService.getGradesForEnrolledCourses();
        log.info("Exiting getGradesForEnrolledCourses with response: {}", response.size());
        return response;
    }

    @GetMapping
    public List<UserToStudentResponseDto> getStudents() {
        List<UserToStudentResponseDto> response = userService.getStudents();
        log.info("Exiting getStudents with response: {}", response.size());
        return response;
    }
}
