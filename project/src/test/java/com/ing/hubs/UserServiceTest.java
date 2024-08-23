package com.ing.hubs;

import com.ing.hubs.dto.UserDto;
import com.ing.hubs.dto.UserResponseDto;
import com.ing.hubs.dto.UserSessionDto;
import com.ing.hubs.entity.User;
import com.ing.hubs.exception.user.DuplicateUsernameException;
import com.ing.hubs.exception.user.InvalidCredentialsException;
import com.ing.hubs.exception.user.InvalidEmailException;
import com.ing.hubs.repository.StudentRepository;
import com.ing.hubs.repository.TeacherRepository;
import com.ing.hubs.repository.UserRepository;
import com.ing.hubs.security.JwtProvider;
import com.ing.hubs.service.UserService;
import com.ing.hubs.validation_rules.Rules;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Rules rule;

    @InjectMocks
    private UserService userService;

    private UserDto userDto;
    private UserResponseDto userResponseDto;

    @BeforeEach
    public void setUp() {
        userDto = new UserDto();
        userDto.setUsername("test");
        userDto.setEmail("v@yahoo.com");
        userDto.setPassword("password");
        userDto.setRole("student");

        userResponseDto = new UserResponseDto();
        userResponseDto.setUsername("test");

        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveUserSuccessfully() {
        userDto.setEmail("test@yahoo.com");

        when(rule.isValidEmail(anyString())).thenReturn(true);
        when(userRepository.save(any(User.class))).thenReturn(new User());
        when(modelMapper.map(any(UserDto.class), eq(User.class))).thenReturn(new User());
        when(modelMapper.map(any(User.class), eq(UserResponseDto.class))).thenReturn(new UserResponseDto());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        UserResponseDto response = userService.saveUser(userDto);

        assertNotNull(response);
        verify(userRepository, times(2)).save(any(User.class));
    }

    @Test
    void saveUserFailsDueToInvalidEmail() {
        userDto.setEmail("test@yahoocom");
        when(rule.isValidEmail(anyString())).thenReturn(false);
        when(modelMapper.map(any(UserDto.class), eq(User.class))).thenReturn(new User());

        InvalidEmailException exception = assertThrows(InvalidEmailException.class, () -> {
            userService.saveUser(userDto);
        });

        assertEquals("Invalid email", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void saveUserFailsDueToDuplicateUsername() {
        User existingUser = new User();
        existingUser.setUsername("test");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(existingUser));
        when(rule.isValidEmail(anyString())).thenReturn(true);
        when(modelMapper.map(any(UserDto.class), eq(User.class))).thenReturn(new User());
        when(userRepository.save(any(User.class))).thenReturn(new User());

        DuplicateUsernameException exception = assertThrows(DuplicateUsernameException.class, () -> {
            userService.saveUser(userDto);
        });

        assertEquals("Duplicate username!", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createSession_invalidCredentials() {
        UserSessionDto userSessionDto = new UserSessionDto();
        userSessionDto.setUsername("test");
        userSessionDto.setPassword("wrong_password");

        doThrow(new org.springframework.security.core.AuthenticationException("Invalid credentials") {})
                .when(authenticationManager)
                .authenticate(any(Authentication.class));

        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> {
            userService.createSession(userSessionDto);
        });

        assertEquals("Invalid credentials! Check username or password!", exception.getMessage());
        verify(userRepository, never()).findByUsername(anyString());
        verify(jwtProvider, never()).generateJwt(any(User.class));
    }
}
