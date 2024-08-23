package com.ing.hubs.service;

import com.ing.hubs.dto.EnrolmentResponseDto;
import com.ing.hubs.entity.*;
import com.ing.hubs.exception.enrolment.EnrolmentNotFoundException;
import com.ing.hubs.exception.enrolment.RequestAlreadyApprovedException;
import com.ing.hubs.exception.enrolment.RequestAlreadyRejectedException;
import com.ing.hubs.exception.teacher.TeacherUnauthorizedException;
import com.ing.hubs.repository.CourseRepository;
import com.ing.hubs.repository.EnrolmentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class EnrolmentService {
    private final ModelMapper modelMapper;
    private final EnrolmentRepository enrolmentRepository;
    private final CourseRepository courseRepository;
    private final CourseService courseService;
    private final TeacherService teacherService;

    public EnrolmentResponseDto rejectEnrolment(Long requestId) {
        log.info("Rejecting enrolment request with id: {}", requestId);
        Enrolment enrolment = processEnrolmentRequest(requestId);

        if (enrolment.getStatus() == EnrolmentStatus.APPROVED) {
            log.warn("Enrolment request with id {} is already approved", requestId);
            throw new RequestAlreadyApprovedException();
        } else if (enrolment.getStatus() == EnrolmentStatus.REJECTED) {
            log.warn("Enrolment request with id {} is already rejected", requestId);
            throw new RequestAlreadyRejectedException();
        }

        updateEnrolmentStatus(enrolment, EnrolmentStatus.REJECTED);
        EnrolmentResponseDto response = modelMapper.map(enrolmentRepository.save(enrolment), EnrolmentResponseDto.class);
        log.info("Enrolment request with id {} rejected successfully", requestId);
        return response;
    }

    public EnrolmentResponseDto acceptEnrolment(Long requestId) {
        log.info("Accepting enrolment request with id: {}", requestId);
        Enrolment enrolment = processEnrolmentRequest(requestId);

        if (enrolment.getStatus() == EnrolmentStatus.APPROVED) {
            log.warn("Enrolment request with id {} is already approved", requestId);
            throw new RequestAlreadyApprovedException();
        } else {
            if (enrolment.getStatus() == EnrolmentStatus.REJECTED) {
                log.warn("Enrolment request with id {} is already rejected", requestId);
                throw new RequestAlreadyRejectedException();
            }
        }

        updateEnrolmentStatus(enrolment, EnrolmentStatus.APPROVED);
        updateCourseMaxAttendees(enrolment.getCourseEnrol());
        EnrolmentResponseDto response = modelMapper.map(enrolmentRepository.save(enrolment), EnrolmentResponseDto.class);
        log.info("Enrolment request with id {} accepted successfully", requestId);
        return response;
    }

    private Enrolment processEnrolmentRequest(Long requestId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Teacher teacher = teacherService.getTeacherByUser(user);
        Enrolment enrolment = getEnrolmentById(requestId);
        Course course = courseService.getCourseById(enrolment.getCourseEnrol().getId());

        if (!course.getTeacher().equals(teacher)) {
            throw new TeacherUnauthorizedException();
        }
        return enrolment;
    }

    private void updateEnrolmentStatus(Enrolment enrolment, EnrolmentStatus status) {
        enrolment.setStatus(status);
    }

    private void updateCourseMaxAttendees(Course course) {
        course.setNoAttendees(course.getNoAttendees() + 1);
        courseRepository.save(course);
    }

    private Enrolment getEnrolmentById(Long id) {
        log.info("Fetching enrolment with id: {}", id);
        return enrolmentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Enrolment with id {} not found", id);
                    return new EnrolmentNotFoundException();
                });
    }

}
