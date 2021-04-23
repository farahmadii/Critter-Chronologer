package com.udacity.jdnd.course3.critter.entity;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="tblOwner")
@Setter @Getter @NoArgsConstructor @EqualsAndHashCode
public class Owner extends User{

    private String phoneNumber;
    private String notes;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner", cascade = CascadeType.ALL, targetEntity = Pet.class)
    private List<Pet> pets;

    public void addPet(Pet pet) {
        pets.add(pet);
    }

}
