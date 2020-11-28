package com.udacity.jdnd.course3.critter.entity;

import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Employee {
    @Id
    @GeneratedValue
    private Long id;

    @Nationalized
    private String name;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<EmployeeSkill> skills = new ArrayList<>();

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<DayOfWeek> daysAvailable = new ArrayList<>();
}
