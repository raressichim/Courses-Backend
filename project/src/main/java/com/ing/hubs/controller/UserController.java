package com.ing.hubs.controller;

import com.ing.hubs.dto.*;
import com.ing.hubs.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/users")
@Slf4j
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @PostMapping
    public UserResponseDto create(@RequestBody @Valid UserDto dto) {
        UserResponseDto response = userService.saveUser(dto);
        log.info("Exiting create with response: {}", response);
        return response;
    }

    @GetMapping
    public List<GetUsersResponseDto> getUsers() {
        List<GetUsersResponseDto> response = userService.getUsers();
        log.info("Exiting getUsers with response: {}", response.size());
        return response;
    }

    @PostMapping("/sessions")
    public JwtDto createSession(@RequestBody @Valid UserSessionDto dto) {
        JwtDto response = userService.createSession(dto);
        log.info("Exiting createSession with response: {}", response);
        return response;
    }

}
