package com.ing.hubs.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int maxAttendees;

    @Column(nullable = false)
    @Builder.Default
    private int noAttendees=0;

    @Column(nullable = false)
    @Builder.Default
    private CourseStatus status = CourseStatus.ONGOING ;

    @ManyToOne
    @JoinColumn(nullable = false)
    @JsonIgnore
    private Teacher teacher;

    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER, cascade = CascadeType.ALL,orphanRemoval = true)
    @Builder.Default
    private Set<Schedule> schedules = new HashSet<>();

    @OneToMany(mappedBy = "courseEnrol",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Builder.Default
    @JsonIgnore
    private List<Enrolment> enrolmentList = new ArrayList<>();

    public void addSchedule(Schedule schedule){
        this.schedules.add(schedule);
        schedule.setCourse(this);
    }

    public void addEnrolment(Enrolment enrolment){
        enrolmentList.add(enrolment);
        enrolment.setCourseEnrol(this);
    }
}
