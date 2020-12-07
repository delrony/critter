package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.pet.PetNotFoundException;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import com.udacity.jdnd.course3.critter.user.CustomerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Transactional
    public Pet savePet(Pet pet, Long customerId) {
        if (customerId != 0) {
            Optional<Customer> optionalCustomer = this.customerRepository.findById(customerId);
            if (!optionalCustomer.isPresent()) {
                throw new CustomerNotFoundException("Customer not found for ID " + customerId);
            }
            Customer customer = optionalCustomer.get();

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
        Optional<Customer> optionalCustomer = this.customerRepository.findById(customerId);
        if (!optionalCustomer.isPresent()) {
            throw new CustomerNotFoundException("Customer not found for ID " + customerId);
        }

        return this.petRepository.findAllByCustomer(optionalCustomer.get());
    }
}
