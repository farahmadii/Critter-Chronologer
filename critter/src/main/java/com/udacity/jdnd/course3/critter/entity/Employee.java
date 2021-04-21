package com.udacity.jdnd.course3.critter.entity;


import javax.persistence.*;
import java.time.DayOfWeek;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name="tblEmployee")
public class Employee extends User{

    @ElementCollection
    @CollectionTable(name="tblDaysAvailable")
    @Enumerated(EnumType.STRING)
    @Column(name = "daysAvailable", nullable = false)
    private Set<DayOfWeek> daysAvailable;

    @ElementCollection
    @CollectionTable(name="tblSkills")
    @Enumerated(EnumType.STRING)
    @Column(name = "skills", nullable = false)
    private Set<EmployeeSkill> skills;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;
}
