package com.ing.hubs.repository;

import com.ing.hubs.entity.ActivityType;
import com.ing.hubs.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule,Long> {
    List<Schedule> findByName(ActivityType name);

}
