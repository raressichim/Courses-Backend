package com.ing.hubs.dto;

import lombok.Data;

import java.util.List;

@Data
public class CourseDto {
    private String name;
    private String description;
    private int maxAttendees;
    private int startYear;
    private int endYear;
    private List<ScheduleDto> schedule;

}

