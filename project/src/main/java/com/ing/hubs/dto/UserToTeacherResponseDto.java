package com.ing.hubs.dto;

import lombok.Data;

@Data
public class UserToTeacherResponseDto {
    private Long id;
    private String firstname;
    private String lastname;
    private String username;
    private Long teacherId;
}
