package com.ing.hubs.service;

import com.ing.hubs.dto.*;
import com.ing.hubs.entity.Student;
import com.ing.hubs.entity.Teacher;
import com.ing.hubs.entity.User;
import com.ing.hubs.exception.user.*;
import com.ing.hubs.repository.StudentRepository;
import com.ing.hubs.repository.TeacherRepository;
import com.ing.hubs.repository.UserRepository;
import com.ing.hubs.security.JwtProvider;
import com.ing.hubs.validation_rules.Rules;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {
    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private StudentRepository studentRepository;
    private TeacherRepository teacherRepository;
    private JwtProvider jwtProvider;
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    private Rules rule;

    @Transactional
    public UserResponseDto saveUser(UserDto userDto) {
        log.info("Saving user with username: {}", userDto.getUsername());
        try {
            validateUser(userDto);

            User userToSave = modelMapper.map(userDto, User.class);
            userToSave.setPassword(passwordEncoder.encode(userDto.getPassword()));

            User savedUser = userRepository.save(userToSave);
            assignRoleToUser(userDto, savedUser);
            log.info("User with username: {} saved successfully", userDto.getUsername());
            return modelMapper.map(savedUser, UserResponseDto.class);
        } catch (DataIntegrityViolationException ex) {
            throw new InvalidUserFormatException();
        }
    }

    private void validateUser(UserDto userDto) {
        log.info("Validating user with username: {}", userDto.getUsername());
        if (!rule.isValidEmail(userDto.getEmail())) {
            log.warn("Invalid email format: {}", userDto.getEmail());
            throw new InvalidEmailException();
        }

        List<User> users = userRepository.findAll();
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(userDto.getUsername())) {
                log.warn("Duplicate username found: {}", userDto.getUsername());
                throw new DuplicateUsernameException();
            }
            if (u.getEmail().equals(userDto.getEmail())) {
                log.warn("Duplicate email found: {}", userDto.getEmail());
                throw new DuplicateEmailException();
            }
        }
    }

    private void assignRoleToUser(UserDto userDto, User savedUser) {
        log.info("Assigning role to user with username: {}", userDto.getUsername());
        if ("student".equalsIgnoreCase(userDto.getRole())) {
            assignStudentRole(userDto, savedUser);
        } else if ("teacher".equalsIgnoreCase(userDto.getRole())) {
            assignTeacherRole(savedUser);
        } else {
            log.warn("Invalid role provided: {}", userDto.getRole());
            throw new InvalidRoleException();
        }
    }

    private void assignStudentRole(UserDto userDto, User savedUser) {
        Student student = Student.builder()
                .userStudent(savedUser)
                .build();
        studentRepository.save(student);
        savedUser.setStudent(student);
        userRepository.save(savedUser);
    }

    private void assignTeacherRole(User savedUser) {
        Teacher teacher = Teacher.builder()
                .userTeacher(savedUser)
                .build();
        teacherRepository.save(teacher);
        savedUser.setTeacher(teacher);
        userRepository.save(savedUser);
    }

    public JwtDto createSession(UserSessionDto userSessionDto) {
        log.info("Creating session for username: {}", userSessionDto.getUsername());
        try {
            authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    userSessionDto.getUsername(),
                                    userSessionDto.getPassword()
                            )
                    );
        } catch (AuthenticationException e) {
            log.error("Invalid credentials for username: {}", userSessionDto.getUsername());
            throw new InvalidCredentialsException();
        }
        var user = userRepository.findByUsername(userSessionDto.getUsername())
                .orElseThrow(() -> {
                    log.error("User not found for username: {}", userSessionDto.getUsername());
                    return new UserNotFoundException();
                });

        var jwt = jwtProvider.generateJwt(user);
        log.info("Session created successfully for username: {}", userSessionDto.getUsername());
        return new JwtDto(jwt);
    }


    public List<GetUsersResponseDto> getUsers() {
        var users = userRepository.findAll();
        var usersToReturn = new ArrayList<GetUsersResponseDto>();
        users.forEach(user -> usersToReturn.add(modelMapper.map(user, GetUsersResponseDto.class)));
        log.info("Found {} users", usersToReturn.size());
        return usersToReturn;
    }

    protected User getById(Long userId) {
        log.info("Fetching user by id: {}", userId);
        return userRepository.findById(userId).orElseThrow(() -> {
            log.error("User not found with id: {}", userId);
            return new UserNotFoundException();
        });
    }

    public List<UserToStudentResponseDto> getStudents() {
        var students = findUserWithStudents();
        var studentList = new ArrayList<UserToStudentResponseDto>();
        students.forEach(student -> studentList.add(modelMapper.map(student, UserToStudentResponseDto.class)));
        log.info("Found {} students", studentList.size());
        return studentList;
    }


    public List<UserToTeacherResponseDto> getTeachers() {
        var teachers = findUserWithTeachers();
        var teacherList = new ArrayList<UserToTeacherResponseDto>();
        teachers.forEach(teacher -> teacherList.add(modelMapper.map(teacher, UserToTeacherResponseDto.class)));
        log.info("Found {} teachers", teacherList.size());
        return teacherList;
    }

    public List<User> findUserWithStudents() {
        return userRepository.findAll().stream()
                .filter(user -> user.getStudent() != null)
                .collect(Collectors.toList());
    }

    public List<User> findUserWithTeachers() {
        return userRepository.findAll().stream()
                .filter(user -> user.getTeacher() != null)
                .collect(Collectors.toList());
    }


}
