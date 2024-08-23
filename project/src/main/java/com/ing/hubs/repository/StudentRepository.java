package com.ing.hubs.repository;

import com.ing.hubs.entity.Enrolment;
import com.ing.hubs.entity.Student;
import com.ing.hubs.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student,Long> {
    Optional<Student> findByUserStudent(User user);
    List<Enrolment> findByEnrolmentList(Student student);
}
