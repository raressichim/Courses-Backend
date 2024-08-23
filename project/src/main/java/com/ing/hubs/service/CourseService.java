package com.ing.hubs.service;

import com.ing.hubs.dto.*;
import com.ing.hubs.entity.*;
import com.ing.hubs.exception.course.CourseIsAlreadyOnGoing;
import com.ing.hubs.exception.course.CourseNotFoundException;
import com.ing.hubs.exception.schedule.CourseScheduleAlreadyExistsException;
import com.ing.hubs.exception.schedule.InvalidEnumException;
import com.ing.hubs.exception.schedule.InvalidFormatException;
import com.ing.hubs.repository.CourseRepository;
import com.ing.hubs.service.TeacherService;
import com.ing.hubs.validation_rules.Rules;
import com.ing.hubs.validation_rules.ValidSchedule;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CourseService {
    private final CourseRepository courseRepository;
    private final TeacherService teacherService;
    private final ModelMapper modelMapper;
    private final ValidSchedule checker;
    private final Rules check;

    @Transactional
    public CourseResponseDto addCourse(CourseDto newCourseDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Teacher teacher = teacherService.getTeacherByUser(user);

        validateCourseAlreadyOnGoing(newCourseDto, teacher);

        log.info("Adding course with name: {}", newCourseDto.getName());

        Course course = createCourse(newCourseDto, teacher);
        List<Schedule> schedules = createSchedules(newCourseDto, course);

        validateCourseSchedules(schedules);

        schedules.forEach(course::addSchedule);
        teacher.addCourse(course);

        Course savedCourse = courseRepository.save(course);
        CourseResponseDto response = modelMapper.map(savedCourse, CourseResponseDto.class);

        log.info("Successfully added course: {}", response);
        return response;
    }

    private void validateCourseAlreadyOnGoing(CourseDto newCourseDto, Teacher teacher) {
        List<Course> courses = courseRepository.findByTeacher(teacher);
        for (Course course : courses) {
            if (course.getName().equals(newCourseDto.getName()) && course.getStatus() == CourseStatus.ONGOING) {
                throw new CourseIsAlreadyOnGoing();
            }
            for (Schedule existingSchedule : course.getSchedules()) {
                for (ScheduleDto newScheduleDto : newCourseDto.getSchedule()) {
                    Schedule newSchedule = createSchedule(newScheduleDto, course);
                    if (checker.checkScheduleOverlap(existingSchedule, newSchedule)) {
                        throw new CourseScheduleAlreadyExistsException();
                    }
                }
            }
        }
    }

    private Course createCourse(CourseDto newCourseDto, Teacher teacher) {
        return Course.builder()
                .name(newCourseDto.getName())
                .description(newCourseDto.getDescription())
                .maxAttendees(newCourseDto.getMaxAttendees())
                .status(CourseStatus.ONGOING)
                .teacher(teacher)
                .build();
    }

    private List<Schedule> createSchedules(CourseDto newCourseDto, Course course) {
        log.info("Creating schedules for course: {}", newCourseDto.getName());
        return newCourseDto.getSchedule().stream()
                .map(dto -> createSchedule(dto, course))
                .collect(Collectors.toList());
    }

    private Schedule createSchedule(ScheduleDto dto, Course course) {
        try {
            Schedule schedule = Schedule.builder()
                    .name(ActivityType.valueOf(dto.getName().toUpperCase()))
                    .startDate(dto.getStartDate())
                    .endDate(dto.getEndDate())
                    .weekDay(WeekDay.valueOf(dto.getWeekDay().toUpperCase()))
                    .startTime(dto.getStartTime())
                    .endTime(dto.getEndTime())
                    .course(course)
                    .build();

            if (!checker.isValidTimeInterval(schedule) || !checker.isValidDateInterval(schedule)) {
                log.error("Invalid schedule format for schedule: {}", dto.getName());
                throw new InvalidFormatException();
            }

            return schedule;
        } catch (IllegalArgumentException e) {
            log.error("Invalid name or week day enum value: {}", dto.getWeekDay());
            throw new InvalidEnumException();
        }
    }

    private void validateCourseSchedules(List<Schedule> schedules) {
        if (!checker.isPresentOnlyOneCourse(schedules)) {
            log.error("Schedule conflict detected");
            throw new CourseScheduleAlreadyExistsException();
        }
    }

    @Transactional
    public CourseResponseDto addLab(LabDto dto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Teacher teacher = teacherService.getTeacherByUser(user);

        log.info("Adding lab to course with name: {}", dto.getName());

        Course course = check.validateLab(dto);
        if (course == null) {
            throw new CourseNotFoundException();
        }
        Schedule schedule = createLabSchedule(dto, course);

        validateLabSchedule(course, schedule);
        validateTeacherSchedules(schedule, teacher);
        course.addSchedule(schedule);
        Course savedCourse = courseRepository.save(course);

        CourseResponseDto response = modelMapper.map(savedCourse, CourseResponseDto.class);
        log.info("Successfully added lab to course: {}", response);
        return response;
    }

    private void validateTeacherSchedules(Schedule newSchedule, Teacher teacher) {
        List<Course> teacherCourses = courseRepository.findByTeacher(teacher);
        for (Course course : teacherCourses) {
            for (Schedule existingSchedule : course.getSchedules()) {
                if (checker.checkScheduleOverlap(existingSchedule, newSchedule)) {
                    throw new CourseScheduleAlreadyExistsException();
                }
            }
        }
    }

    private Schedule createLabSchedule(LabDto dto, Course course) {
        LocalDate start = null, end = null;
        for (Schedule schedule : course.getSchedules()) {
            if (schedule.getName().equals(ActivityType.COURSE)) {
                start = schedule.getStartDate();
                end = schedule.getEndDate();
                break;
            }
        }

        return Schedule.builder()
                .name(ActivityType.LABORATORY)
                .startDate(start)
                .endDate(end)
                .weekDay(WeekDay.valueOf(dto.getSchedule().getWeekDay().toUpperCase()))
                .startTime(dto.getSchedule().getStartTime())
                .endTime(dto.getSchedule().getEndTime())
                .course(course)
                .build();
    }

    private void validateLabSchedule(Course course, Schedule schedule) {
        if (!checker.isValidTimeInterval(schedule) || !checker.isValidDateInterval(schedule)) {
            log.error("Invalid lab schedule format ");
            throw new InvalidFormatException();
        }
    }

    public List<CourseResponseDto> getCourses() {
        log.info("Fetching all courses");
        var courses = courseRepository.findAll();
        List<CourseResponseDto> response = courses.stream()
                .map(course -> modelMapper.map(course, CourseResponseDto.class))
                .collect(Collectors.toList());
        log.info("Exiting getCourses with list size: {}", response.size());
        return response;
    }

    protected Course getCourseById(Long id) {
        log.info("Fetching course by ID: {}", id);
        return courseRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Course not found with ID: {}", id);
                    return new CourseNotFoundException();
                });
    }

    @Transactional
    public CourseResponseDto disableCourse(CourseToDisableDto dto) {
        Course course = check.validateDisablingCourse(dto);

        course.getSchedules().clear();
        course.setStatus(CourseStatus.DISABLED);

        Course savedCourse = courseRepository.save(course);
        return modelMapper.map(savedCourse, CourseResponseDto.class);
    }

    public List<CourseResponseDto> getCoursesForTeacher() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Teacher teacher = teacherService.getTeacherByUser(user);
        log.info("Fetching all courses");
        var courses = courseRepository.findByTeacher(teacher);
        List<CourseResponseDto> response = courses.stream()
                .map(course -> modelMapper.map(course, CourseResponseDto.class))
                .collect(Collectors.toList());
        log.info("Exiting getCoursesForTeacher with list size: {}", response.size());
        return response;
    }
}
