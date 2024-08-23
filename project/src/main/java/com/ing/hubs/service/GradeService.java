package com.ing.hubs.service;

import com.ing.hubs.dto.EnrolmentResponseDto;
import com.ing.hubs.entity.*;
import com.ing.hubs.exception.student.StudentIsntEnrolledException;
import com.ing.hubs.exception.teacher.AlreadyGradedException;
import com.ing.hubs.repository.EnrolmentRepository;
import com.ing.hubs.validation_rules.Rules;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class GradeService {
    private final EnrolmentRepository enrolmentRepository;
    private final UserService userService;
    private final TeacherService teacherService;
    private final StudentService studentService;
    private final CourseService courseService;
    private final ModelMapper modelMapper;
    private Rules validationRule;

    public EnrolmentResponseDto gradeStudent(Long courseId, Long studentUserId, double grade) {
        log.info("Grading student with userId {} for courseId {} with grade {}", studentUserId, courseId, grade);
        validationRule.isValidGrade(grade);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Teacher teacher = teacherService.getTeacherByUser(user);

        User userStudent = userService.getById(studentUserId);
        Student student = studentService.getStudentByUser(userStudent);

        Course course = courseService.getCourseById(courseId);

        validationRule.validateTeacherForCourse(teacher, course);

        Enrolment enrolment = getApprovedEnrolment(course, student);

        if (enrolment.getGrade() == 0.0) {
            log.info("Grade {} set for student with userId {} in courseId {}", grade, studentUserId, courseId);
            enrolment.setGrade(grade);
        } else {
            log.warn("Student with userId {} in courseId {} is already graded", studentUserId, courseId);
            throw new AlreadyGradedException();
        }


        EnrolmentResponseDto response = modelMapper.map(enrolmentRepository.save(enrolment), EnrolmentResponseDto.class);
        log.info("Grade saved successfully for student with userId {} in courseId {}", studentUserId, courseId);
        return response;
    }


    protected Enrolment getApprovedEnrolment(Course course, Student student) {
        Enrolment enrolment = enrolmentRepository.findByCourseEnrolAndStudentEnrol(course, student)
                .orElseThrow(() -> {
                    log.error("Student with id {} is not enrolled in courseId {}", student.getId(), course.getId());
                    return new StudentIsntEnrolledException();
                });

        if (enrolment.getStatus() != EnrolmentStatus.APPROVED) {
            log.warn("Enrolment for studentId {} in courseId {} is not approved", student.getId(), course.getId());
            throw new StudentIsntEnrolledException();
        }
        return enrolment;
    }

}
