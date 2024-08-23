package com.ing.hubs.validation_rules;

import com.ing.hubs.entity.ActivityType;
import com.ing.hubs.entity.Schedule;
import com.ing.hubs.repository.CourseRepository;
import com.ing.hubs.repository.ScheduleRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Data
@AllArgsConstructor
public class ValidSchedule {
    private final ScheduleRepository scheduleRepository;
    private final CourseRepository courseRepository;

    public boolean isPresentOnlyOneCourse(List<Schedule> schedules) {
        List<String> unique = new ArrayList<>();

        for (Schedule schedule : schedules) {
            if (schedule.getName().equals(ActivityType.COURSE)) {
                unique.add(schedule.getName().toString());
            }
        }
        return unique.size() == 1;
    }

    public boolean isValidDateInterval(Schedule schedule) {
        return schedule.getStartDate().isBefore(schedule.getEndDate());
    }

    public boolean isValidTimeInterval(Schedule schedule) {
        return schedule.getStartTime().isBefore(schedule.getEndTime());
    }

    public boolean checkDateIntervalOverlap(Schedule schedule1, Schedule schedule2) {
        return !(schedule1.getEndDate().isBefore(schedule2.getStartDate()) ||
                schedule1.getStartDate().isAfter(schedule2.getEndDate()));
    }

    public boolean checkTimeIntervalOverlap(Schedule schedule1, Schedule schedule2) {
        return !(schedule1.getEndTime().isBefore(schedule2.getStartTime()) ||
                schedule1.getStartTime().isAfter(schedule2.getEndTime()));
    }

    public boolean checkScheduleOverlap(Schedule schedule1, Schedule schedule2) {
        if (!schedule1.getWeekDay().equals(schedule2.getWeekDay())) {
            return false;
        }
        if (checkDateIntervalOverlap(schedule1, schedule2)) {
            return checkTimeIntervalOverlap(schedule1, schedule2);
        }
        return false;
    }
}
