package com.ing.hubs.repository;

import com.ing.hubs.entity.Course;
import com.ing.hubs.entity.Enrolment;
import com.ing.hubs.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrolmentRepository extends JpaRepository<Enrolment, Long> {
    List<Enrolment> findByCourseEnrol(Course course);

    List<Enrolment> findByStudentEnrol(Student student);

    Optional<Enrolment> findByCourseEnrolAndStudentEnrol(Course course, Student student);

    boolean existsByStudentEnrolAndCourseEnrol(Student student, Course course);
}
