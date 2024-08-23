package com.ing.hubs.repository;

import com.ing.hubs.entity.Teacher;
import com.ing.hubs.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher,Long> {
    Optional<Teacher> findByUserTeacher(User user);
}
