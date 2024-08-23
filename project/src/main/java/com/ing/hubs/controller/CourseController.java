package com.ing.hubs.controller;

import com.ing.hubs.dto.CourseResponseDto;
import com.ing.hubs.dto.CourseDto;
import com.ing.hubs.dto.CourseToDisableDto;
import com.ing.hubs.dto.LabDto;
import com.ing.hubs.service.CourseService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@Slf4j
@AllArgsConstructor
public class CourseController {
    private CourseService courseService;

    @GetMapping("/me")
    public List<CourseResponseDto> getCoursesForTeacher() {
        List<CourseResponseDto> response = courseService.getCoursesForTeacher();
        log.info("Exiting getCoursesForTeacher with response: {}", response.size());
        return response;
    }

    @PostMapping("/me")
    public CourseResponseDto addCourse(@Valid @RequestBody CourseDto courseCreationDto) {
        CourseResponseDto response = courseService.addCourse(courseCreationDto);
        log.info("Exiting addCourse with response: {}", response);
        return response;
    }

    @PostMapping("/labs/me")
    public CourseResponseDto addLab(@Valid @RequestBody LabDto dto) {
        CourseResponseDto response = courseService.addLab(dto);
        log.info("Exiting addLab with response: {}", response);
        return response;
    }

    @GetMapping
    public List<CourseResponseDto> getCourses() {
        List<CourseResponseDto> response = courseService.getCourses();
        log.info("Exiting getCourses with response: {}", response.size());
        return response;
    }

    @PutMapping("/me/disable")
    public CourseResponseDto disableCourse(@Valid @RequestBody CourseToDisableDto dto) {
        CourseResponseDto response = courseService.disableCourse(dto);
        log.info("Exiting disable with response: {}", response);
        return response;

    }

}
