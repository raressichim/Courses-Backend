package com.ing.hubs;

import com.ing.hubs.dto.CourseResponseDto;
import com.ing.hubs.dto.EnrolmentResponseDto;
import com.ing.hubs.entity.*;
import com.ing.hubs.exception.teacher.TeacherNotFoundException;
import com.ing.hubs.exception.user.InvalidEmailException;
import com.ing.hubs.exception.user.UserNotFoundException;
import com.ing.hubs.repository.CourseRepository;
import com.ing.hubs.repository.EnrolmentRepository;
import com.ing.hubs.repository.TeacherRepository;
import com.ing.hubs.service.TeacherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private EnrolmentRepository enrolmentRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private TeacherService teacherService;


    @Test
    void getTeacherByUser() {
        User user = new User();
        Teacher teacher = new Teacher();

        when(teacherRepository.findByUserTeacher(any())).thenReturn(Optional.of(teacher));

        Teacher result = teacherService.getTeacherByUser(user);

        assertNotNull(result);
        verify(teacherRepository, times(1)).findByUserTeacher(any());
    }


    @Test
    void getPendingCoursesRequestsForTeacher() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(new User(), null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Teacher teacher = new Teacher();
        teacher.setId(1L);

        Course course1 = new Course();
        Course course2 = new Course();

        when(teacherRepository.findByUserTeacher(any())).thenReturn(Optional.of(teacher));

        List<CourseResponseDto> result = teacherService.getCoursesForTeacher();

        assertEquals(0, result.size(), "Expected no pending courses for the teacher");
        verify(courseRepository, times(1)).findByTeacher(any());
    }

}

