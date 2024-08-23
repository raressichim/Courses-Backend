package com.ing.hubs.dto;

import com.ing.hubs.entity.EnrolmentStatus;
import com.ing.hubs.entity.Student;
import lombok.Data;

@Data
public class EnrolmentResponseDto {
    private Long id;
    private EnrolmentStatus status;
    private float grade;
    private String courseName;
    private StudentEnrolDto student;
}
