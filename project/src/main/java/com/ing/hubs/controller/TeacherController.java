package com.ing.hubs.controller;

import com.ing.hubs.dto.CourseResponseDto;
import com.ing.hubs.dto.EnrolmentResponseDto;
import com.ing.hubs.dto.UserToTeacherResponseDto;
import com.ing.hubs.service.TeacherService;
import com.ing.hubs.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
@Slf4j
@AllArgsConstructor
public class TeacherController {
    private final UserService userService;
    private TeacherService teacherService;

    @GetMapping
    public List<UserToTeacherResponseDto> getTeachers() {
        List<UserToTeacherResponseDto> response = userService.getTeachers();
        log.info("Exiting getTeachers with response: {}", response.size());
        return response;
    }

    @GetMapping("/me/requests")
    @PreAuthorize("hasAuthority('TEACHER')")
    public List<EnrolmentResponseDto> getPendingCoursesRequestsForTeacher() {
        List<EnrolmentResponseDto> response = teacherService.getPendingCoursesRequestsForTeacher();
        log.info("Exiting getPendingCoursesRequestsForTeacher with response: {}", response.size());
        return response;
    }

    @GetMapping("/me/courses")
    @PreAuthorize("hasAuthority('TEACHER')")
    public List<CourseResponseDto> getCoursesForTeacher() {
        List<CourseResponseDto> response = teacherService.getCoursesForTeacher();
        log.info("Exiting getCoursesForTeacher with response: {}", response.size());
        return response;
    }
}
