package com.udacity.jdnd.course3.critter.entity;


import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name="tblPet")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long petId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private PetType type;

    @Nationalized
    private String name;

    //don't retrieve the owner if we don't need it
    @OneToOne(fetch = FetchType.LAZY)//many pets can belong to one owner
    @JoinColumn(name = "owner_id")  //map the join column in the plant table
    private Owner owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    private LocalDate birthDate;

    private String notes;
}
