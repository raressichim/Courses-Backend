package com.ing.hubs.dto;

import com.ing.hubs.entity.CourseStatus;
import lombok.Data;

import java.util.List;

@Data
public class CourseResponseDto {
    private Long id;
    private String name;
    private String description;
    private int maxAttendees;
    private CourseStatus status;
    private List<ScheduleDto> schedules;
}
