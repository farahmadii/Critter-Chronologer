package com.udacity.jdnd.course3.critter.entity;


import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="tblSchedule")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(fetch = FetchType.LAZY , mappedBy = "schedule", cascade = CascadeType.ALL)//many employees can belong to one event
    private List<Employee> employees;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "schedule", cascade = CascadeType.ALL)
    private List<Pet> pets;

    private LocalDate date;

    @ElementCollection
    @CollectionTable(name="tblActivities")
    @Enumerated(EnumType.STRING)
    @Column(name = "activities", nullable = false)
    private Set<EmployeeSkill> activities;
}
