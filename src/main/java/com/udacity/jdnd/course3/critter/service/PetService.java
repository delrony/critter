package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PetService {
    @Autowired
    PetRepository petRepository;

    @Autowired
    CustomerRepository customerRepository;

    public PetService(PetRepository petRepository, CustomerRepository customerRepository) {
        this.petRepository = petRepository;
        this.customerRepository = customerRepository;
    }

    public Pet savePet(Pet pet, Long customerId) {
        if (customerId != 0) {
            Customer customer = this.customerRepository.getOne(customerId);
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
            this.customerRepository.save(customer);
        }

        return pet;
    }

    public Pet getPet(Long petId) {
        return this.petRepository.getOne(petId);
    }

    public List<Pet> getAllPets() {
        return this.petRepository.findAll();
    }

    public List<Pet> getAllPetsByOwner(Long customerId) {
        Customer customer = this.customerRepository.getOne(customerId);
        return this.petRepository.findAllByCustomer(customer);
    }
}
