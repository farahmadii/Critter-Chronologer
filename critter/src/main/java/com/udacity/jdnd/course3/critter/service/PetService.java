package com.udacity.jdnd.course3.critter.service;


import com.udacity.jdnd.course3.critter.entity.Owner;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.exception.ResourceNotFoundException;
import com.udacity.jdnd.course3.critter.repository.OwnerRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.ReadOnlyFileSystemException;
import java.util.List;
import java.util.Optional;

@Service
public class PetService {

    @Autowired
    PetRepository petRepository;

    @Autowired
    OwnerRepository ownerRepository;

    public Pet savePet(Pet pet){
        return petRepository.save(pet);
    }

    // finding the pet with the given id, and if it's not already recorded in DB throw proper exception
    public Pet getPetByPetId(Long petId){
        Optional<Pet> optionalPet = this.petRepository.findById(petId);
        if(optionalPet.isPresent()){
            return optionalPet.get();
        }
        return optionalPet.orElseThrow(() -> new ResourceNotFoundException("Pet with id: "+ petId + " not found"));
    }

    public List<Pet> getPetsOfAnOwner(Long ownerId){
        return this.petRepository.findPetsByOwnerId(ownerId);

    }

    public List<Pet> findAllPets(){
        return this.petRepository.findAll();
    }
}
