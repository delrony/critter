package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.PetService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {
    @Autowired
    PetService petService;

    @Autowired
    CustomerService customerService;

    public PetController(PetService petService, CustomerService customerService) {
        this.petService = petService;
        this.customerService = customerService;
    }

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        Pet pet = new Pet();
        BeanUtils.copyProperties(petDTO, pet, "id");

        pet = this.petService.savePet(pet, petDTO.getOwnerId());

        PetDTO newPetDTO = this.getDTOFromPet(pet);
        return newPetDTO;
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        Pet pet = this.petService.getPet(petId);

        PetDTO petDTO = this.getDTOFromPet(pet);
        return petDTO;
    }

    @GetMapping
    public List<PetDTO> getPets(){
        List<Pet> pets = this.petService.getAllPets();

        List<PetDTO> petDTOList = new ArrayList<>();
        pets.forEach(pet -> {
            PetDTO petDTO = this.getDTOFromPet(pet);
            petDTOList.add(petDTO);
        });

        return petDTOList;
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        List<Pet> pets = this.petService.getAllPetsByOwner(ownerId);

        List<PetDTO> petDTOList = new ArrayList<>();
        pets.forEach(pet -> {
            PetDTO petDTO = this.getDTOFromPet(pet);
            petDTOList.add(petDTO);
        });

        return petDTOList;
    }

    private PetDTO getDTOFromPet(Pet pet) {
        PetDTO petDTO = new PetDTO();
        BeanUtils.copyProperties(pet, petDTO);

        Customer customer = pet.getCustomer();
        if (customer != null) {
            petDTO.setOwnerId(pet.getCustomer().getId());
        }

        return petDTO;
    }
}
