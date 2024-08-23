package com.ing.hubs.service;

import com.ing.hubs.dto.CourseResponseDto;
import com.ing.hubs.dto.EnrolmentResponseDto;
import com.ing.hubs.entity.*;
import com.ing.hubs.exception.teacher.TeacherNotFoundException;
import com.ing.hubs.repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final EnrolmentRepository enrolmentRepository;
    private final ModelMapper modelMapper;
    private final CourseRepository courseRepository;


    public List<EnrolmentResponseDto> getPendingCoursesRequestsForTeacher() {
        log.info("Fetching pending course requests for the authenticated teacher");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Teacher teacher = getTeacherByUser(user);
        List<EnrolmentResponseDto> coursesRequests = new ArrayList<>();

        for (Course course : teacher.getCourses()) {
            List<Enrolment> enrolmentList = enrolmentRepository.findByCourseEnrol(course);
            enrolmentList.forEach(enrolment -> {
                if (enrolment.getStatus() == EnrolmentStatus.PENDING) {
                    coursesRequests.add(modelMapper.map(enrolment, EnrolmentResponseDto.class));
                }
            });
        }
        log.info("Found {} pending course requests for teacher with id {}", coursesRequests.size(), teacher.getId());
        return coursesRequests;
    }

    public Teacher getTeacherByUser(User user) {
        log.info("Fetching teacher by user id {}", user.getId());
        return teacherRepository.findByUserTeacher(user)
                .orElseThrow(() -> {
                    log.error("Teacher not found for user id {}", user.getId());
                    return new TeacherNotFoundException();
                });
    }

    public List<CourseResponseDto> getCoursesForTeacher() {
        log.info("Fetching courses for the authenticated teacher");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Teacher teacher = getTeacherByUser(user);
        List<Course> courses = courseRepository.findByTeacher(teacher);
        List<CourseResponseDto> list = new ArrayList<>();
        courses.stream().map(course -> list.add(modelMapper.map(course, CourseResponseDto.class)));
        log.info("Found {} courses for teacher with id {}", list.size(), teacher.getId());
        return list;
    }
}
