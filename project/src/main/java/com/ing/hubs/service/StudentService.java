package com.ing.hubs.service;

import com.ing.hubs.dto.EnrolmentDto;
import com.ing.hubs.dto.EnrolmentResponseDto;
import com.ing.hubs.dto.EnrolmentStudentResponseDto;
import com.ing.hubs.entity.*;
import com.ing.hubs.exception.course.CannotEnrolInDisabledCourseException;
import com.ing.hubs.exception.course.CourseFullException;
import com.ing.hubs.exception.course.CourseNotFoundException;
import com.ing.hubs.exception.enrolment.EnrolmentAlreadyExistsException;
import com.ing.hubs.exception.schedule.OverlappingSchedulesException;
import com.ing.hubs.exception.student.StudentNotFoundException;
import com.ing.hubs.repository.CourseRepository;
import com.ing.hubs.repository.EnrolmentRepository;
import com.ing.hubs.repository.StudentRepository;
import com.ing.hubs.validation_rules.Rules;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class StudentService {
    private final ModelMapper modelMapper;
    private final StudentRepository studentRepository;
    private final EnrolmentRepository enrolmentRepository;
    private final Rules check;
    private final CourseRepository courseRepository;

    @Transactional
    public EnrolmentResponseDto requestEnrol(EnrolmentDto enrolmentDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Student student = getStudentByUser(user);

        log.info("Retrieved student: {}", student.getId());

        Course course = getCourseByName(enrolmentDto);

        log.info("Retrieved course: {}", course.getId());

        validateEnrolment(student, course);

        Enrolment enrolment = createEnrolment(student, course);

        Enrolment savedEnrolment = enrolmentRepository.save(enrolment);

        log.info("Enrolment created successfully for student {} in course {}", student.getId(), course.getId());
        student.addEnrolment(enrolment);
        return modelMapper.map(savedEnrolment, EnrolmentResponseDto.class);
    }

    private void validateEnrolment(Student student, Course course) {
        if (enrolmentRepository.existsByStudentEnrolAndCourseEnrol(student, course)) {
            log.warn("Enrolment already exists for student {} in course {}", student.getId(), course.getId());
            throw new EnrolmentAlreadyExistsException();
        }

        if (course.getMaxAttendees() == course.getNoAttendees()) {
            log.warn("Course {} is full", course.getId());
            throw new CourseFullException();
        }

        if (!check.validateEnrolmentTimeFrame(student, course)) {
            log.warn("Schedules overlap for student {} and course {}", student.getId(), course.getId());
            throw new OverlappingSchedulesException();
        }
    }

    private Enrolment createEnrolment(Student student, Course course) {
        return Enrolment.builder()
                .studentEnrol(student)
                .courseEnrol(course)
                .status(EnrolmentStatus.PENDING)
                .grade(0.0)
                .build();
    }

    public List<EnrolmentStudentResponseDto> getGradesForEnrolledCourses() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Student student = getStudentByUser(user);

        List<Enrolment> approvedEnrolments = enrolmentRepository.findByStudentEnrol(student)
                .stream()
                .filter(enrolment -> enrolment.getStatus() == EnrolmentStatus.APPROVED)
                .toList();

        return mapToEnrolmentStudentResponseDtoList(approvedEnrolments);
    }

    private List<EnrolmentStudentResponseDto> mapToEnrolmentStudentResponseDtoList(List<Enrolment> enrolments) {
        List<EnrolmentStudentResponseDto> enrolmentsToReturn = new ArrayList<>();
        enrolments.forEach(enrolment ->
                enrolmentsToReturn.add(modelMapper.map(enrolment, EnrolmentStudentResponseDto.class)));
        return enrolmentsToReturn;
    }

    public List<EnrolmentResponseDto> getEnrolmentList() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Student student = getStudentByUser(user);

        List<Enrolment> studentEnrolments = enrolmentRepository.findByStudentEnrol(student).stream().toList();

        return mapToEnrolmentResponseDtoList(studentEnrolments);
    }

    private List<EnrolmentResponseDto> mapToEnrolmentResponseDtoList(List<Enrolment> enrolments) {
        List<EnrolmentResponseDto> enrolmentsToReturn = new ArrayList<>();
        enrolments.forEach(enrolment ->
                enrolmentsToReturn.add(modelMapper.map(enrolment, EnrolmentResponseDto.class)));
        return enrolmentsToReturn;
    }

    protected Student getStudentByUser(User user) {
        return studentRepository.findByUserStudent(user)
                .orElseThrow(() -> {
                    log.error("Student not found for user {}", user.getUsername());
                    return new StudentNotFoundException();
                });
    }

    public Course getCourseByName(EnrolmentDto enrolmentDto) {
        List<Course> courses = courseRepository.findAllByName(enrolmentDto.getCourseName());
        if (courses.isEmpty()) throw new CourseNotFoundException();

        Course courseToreturn = null;
        for (Course course : courses) {
            if (course.getName().equals(enrolmentDto.getCourseName())) {
                if (course.getStatus().equals(CourseStatus.ONGOING)) {

                    courseToreturn = course;
                    break;
                } else throw new CannotEnrolInDisabledCourseException();
            } else throw new CourseNotFoundException();
        }
        return courseToreturn;
    }
}
