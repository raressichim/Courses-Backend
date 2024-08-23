package com.ing.hubs.controller;

import com.ing.hubs.dto.EnrolmentDto;
import com.ing.hubs.dto.EnrolmentResponseDto;
import com.ing.hubs.service.EnrolmentService;
import com.ing.hubs.service.StudentService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrolments")
@Slf4j
@AllArgsConstructor
public class EnrolmentController {
    private final StudentService studentService;
    private final EnrolmentService enrolmentService;

    @PostMapping("/me")
    @PreAuthorize("hasAuthority('STUDENT')")
    public EnrolmentResponseDto requestEnrol(@RequestBody EnrolmentDto enrolmentDto) {
        EnrolmentResponseDto response = studentService.requestEnrol(enrolmentDto);
        log.info("Exiting requestEnrol with response: {}", response);
        return response;
    }

    @GetMapping("/me")
    @PreAuthorize("hasAuthority('STUDENT')")
    public List<EnrolmentResponseDto> getEnrolmentList() {
        List<EnrolmentResponseDto> response = studentService.getEnrolmentList();
        log.info("Exiting getEnrolmentList with response: {}", response.size());
        return response;
    }

    @PutMapping("/me/requests/{requestId}/accept")
    @PreAuthorize("hasAuthority('TEACHER')")
    public EnrolmentResponseDto acceptRequest(@PathVariable Long requestId) {
        EnrolmentResponseDto response = enrolmentService.acceptEnrolment(requestId);
        log.info("Exiting acceptRequest with response: {}", response);
        return response;
    }

    @PutMapping("/me/requests/{requestId}/refuse")
    @PreAuthorize("hasAuthority('TEACHER')")
    public EnrolmentResponseDto refuseRequest(@PathVariable Long requestId) {
        EnrolmentResponseDto response = enrolmentService.rejectEnrolment(requestId);
        log.info("Exiting refuseRequest with response: {}", response);
        return response;
    }

}
