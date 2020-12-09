package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.pet.PetNotFoundException;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PetService {
    @Autowired
    PetRepository petRepository;

    @Autowired
    CustomerService customerService;

    public PetService(PetRepository petRepository, CustomerService customerService) {
        this.petRepository = petRepository;
        this.customerService = customerService;
    }

    public Pet savePet(Pet pet, Long customerId) {
        if (customerId != 0) {
            Customer customer = customerService.getCustomer(customerId);

            pet.setCustomer(customer);
            pet = this.petRepository.save(pet);

            List<Pet> pets = customer.getPets();
            if (pets != null) {
                pets.add(pet);
            } else {
                pets = new ArrayList<>();
                pets.add(pet);
            }
            customer.setPets(pets);
            this.customerService.saveCustomer(customer);
        } else {
            pet = this.petRepository.save(pet);
        }

        return pet;
    }

    public Pet getPet(Long petId) {
        Optional<Pet> optionalPet = this.petRepository.findById(petId);
        if (!optionalPet.isPresent()) {
            throw new PetNotFoundException("Pet not found for ID " + petId);
        }

        return optionalPet.get();
    }

    public List<Pet> getAllPets() {
        return this.petRepository.findAll();
    }

    public List<Pet> getAllPetsByOwner(Long customerId) {
        Customer customer = this.customerService.getCustomer(customerId);

        return this.petRepository.findAllByCustomer(customer);
    }
}
