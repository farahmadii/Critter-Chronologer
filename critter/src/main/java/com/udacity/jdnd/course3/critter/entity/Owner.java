package com.udacity.jdnd.course3.critter.entity;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="tblOwner")
public class Owner extends User{

    private String phoneNumber;
    private String notes;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Pet> pets;
}
