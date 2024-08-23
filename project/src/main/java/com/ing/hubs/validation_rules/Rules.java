package com.ing.hubs.validation_rules;

import com.ing.hubs.dto.CourseToDisableDto;
import com.ing.hubs.dto.LabDto;
import com.ing.hubs.entity.*;
import com.ing.hubs.exception.course.CourseAlreadyDisabledException;
import com.ing.hubs.exception.course.CourseNotFoundException;
import com.ing.hubs.exception.teacher.InvalidGradeException;
import com.ing.hubs.exception.teacher.TeacherUnauthorizedException;
import com.ing.hubs.repository.CourseRepository;
import com.ing.hubs.repository.EnrolmentRepository;
import com.ing.hubs.service.TeacherService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Data
@AllArgsConstructor
public class Rules {
    private EnrolmentRepository enrolmentRepository;
    private CourseRepository courseRepository;
    private TeacherService teacherService;
    private ValidSchedule check;
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.){1,}[a-zA-Z]{2,7}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);

    public boolean isValidEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

    public void isValidGrade(double grade) {
        if (grade < 1 || grade > 10) {
            throw new InvalidGradeException();
        }
    }

    public void validateTeacherForCourse(Teacher teacher, Course course) {
        if (!course.getTeacher().equals(teacher)) {
            throw new TeacherUnauthorizedException();
        }
    }

    public boolean validateEnrolmentTimeFrame(Student student, Course course) {
        System.out.println("Validating enrolment for student: " + student.getId() + " and course: " + course.getId());

        List<Enrolment> enrolments = enrolmentRepository.findByStudentEnrol(student);
        for (Enrolment e : enrolments) {
            Course enrolledCourse = e.getCourseEnrol();
            for (Schedule schedule : enrolledCourse.getSchedules()) {
                for (Schedule aux : course.getSchedules()) {
                    if (schedule.getWeekDay().equals(aux.getWeekDay())) {
                        if ((aux.getName().equals(ActivityType.LABORATORY) && schedule.getName().equals(ActivityType.LABORATORY)) ||
                                (aux.getName().equals(ActivityType.COURSE))) {
                            if (check.checkScheduleOverlap(schedule, aux)) {
                                System.out.println("Schedules overlap found for student: " + student.getId() + " in course: " + course.getId());
                                return false;
                            }
                        }
                    }
                }
            }
        }
        System.out.println("No schedule overlap for student: " + student.getId() + " in course: " + course.getId());
        return true;
    }



    public Course validateLab(LabDto labDto) {
        List<Course> courses = courseRepository.findAllByName(labDto.getName());
        if (courses.isEmpty()) throw new CourseNotFoundException();
        for (Course c : courses) {
            if (c.getName().equals(labDto.getName()) && c.getStatus() == CourseStatus.ONGOING) {
                return c;
            }
        }
        throw new CourseNotFoundException();
    }

    public Course validateDisablingCourse(CourseToDisableDto dto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Teacher teacher = teacherService.getTeacherByUser(user);
        List<Course> courses = courseRepository.findByTeacher(teacher);
        for (Course c : courses) {
            if (c.getName().equals(dto.getName()) && c.getStatus() == CourseStatus.ONGOING) {
                return c;
            }
        }
        throw new CourseAlreadyDisabledException();
    }
}
