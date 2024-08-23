package com.ing.hubs.repository;

import com.ing.hubs.entity.Course;
import com.ing.hubs.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course,Long> {
    Optional<Course> findByName(String name);
    List<Course> findByTeacher(Teacher teacher);
    List<Course> findAllByName(String name);


}
