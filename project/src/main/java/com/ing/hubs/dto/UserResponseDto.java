package com.ing.hubs.dto;

import com.ing.hubs.entity.Student;
import com.ing.hubs.entity.Teacher;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private Student student;
    private Teacher teacher;
}
