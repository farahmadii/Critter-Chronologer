package com.udacity.jdnd.course3.critter.entity;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="tblSchedule")
@Setter @Getter @NoArgsConstructor @EqualsAndHashCode
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //fetch = FetchType.LAZY , mappedBy = "schedule", cascade = CascadeType.ALL
    @ManyToMany(targetEntity = Employee.class)//many employees can belong to one event
    private List<Employee> employees;

    //fetch = FetchType.LAZY, mappedBy = "schedule", cascade = CascadeType.ALL
    @ManyToMany(targetEntity = Pet.class)
    private List<Pet> pets;

    private LocalDate date;

    @ElementCollection(targetClass = EmployeeSkill.class)
    @CollectionTable(name="tblActivities")
    @Enumerated(EnumType.STRING)
    @Column(name = "activities", nullable = false)
    private Set<EmployeeSkill> activities;

}
