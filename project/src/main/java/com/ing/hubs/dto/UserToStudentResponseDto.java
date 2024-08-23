package com.ing.hubs.dto;

import lombok.Data;

@Data
public class UserToStudentResponseDto {
    private Long id;
    private String firstname;
    private String lastname;
    private String username;
    private Long studentId;
}
