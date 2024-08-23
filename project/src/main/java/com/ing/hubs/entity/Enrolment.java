package com.ing.hubs.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Enrolment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Builder.Default
    private EnrolmentStatus status = EnrolmentStatus.PENDING;

    @Column(nullable = false)
    @Builder.Default
    private double grade = 0.0;

    @ManyToOne
    @JoinColumn(name = "student_id")
    @JsonIgnore
    private Student studentEnrol;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course courseEnrol;
}
