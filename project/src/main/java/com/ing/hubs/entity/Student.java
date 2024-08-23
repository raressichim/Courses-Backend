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
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "student",fetch = FetchType.EAGER)
    @JsonIgnore
    private User userStudent;

    @OneToMany(mappedBy = "studentEnrol",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Enrolment> enrolmentList = new ArrayList<>();

    public void addEnrolment(Enrolment enrolment){
        enrolmentList.add(enrolment);
        enrolment.setStudentEnrol(this);
    }

}
