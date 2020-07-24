package com.udacity.jdnd.course3.critter.controllers;

import com.udacity.jdnd.course3.critter.dtos.PetDTO;
import com.udacity.jdnd.course3.critter.model.persistence.entities.Customer;
import com.udacity.jdnd.course3.critter.model.persistence.entities.Pet;
import com.udacity.jdnd.course3.critter.model.persistence.repositories.CustomerRepository;
import com.udacity.jdnd.course3.critter.model.persistence.repositories.PetRepository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {

    private PetRepository petRepository;
    private CustomerRepository customerRepository;

    public PetController(PetRepository petRepository, CustomerRepository customerRepository) {
        this.petRepository = petRepository;
        this.customerRepository = customerRepository;
    }

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {

        Pet pet = new Pet();

        pet.setId(petDTO.getId());
        pet.setPetName(petDTO.getName());
        pet.setPetBirthDate(petDTO.getBirthDate());

        Customer customer = customerRepository.getOne(petDTO.getOwnerId());

        pet.setCustomer(customer);
        pet.setPetNotes(petDTO.getNotes());
        pet.setPetType(petDTO.getType());

        Pet savedPet = petRepository.save(pet);

        List<Pet> pets = new ArrayList<>();
        pets.add(pet);

        customer.setPets(pets);
        customerRepository.save(customer);

        return getDTO(savedPet);
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        Pet pet = petRepository.getOne(petId);
        return getDTO(pet);
    }

    @GetMapping
    public List<PetDTO> getPets(){
        return petRepository.findAll().stream().map(this::getDTO).collect(Collectors.toList());
    }

    private PetDTO getDTO(Pet pet) {
        PetDTO petDTO = new PetDTO();

        petDTO.setId(pet.getId());
        petDTO.setName(pet.getPetName());
        petDTO.setBirthDate(pet.getPetBirthDate());
        petDTO.setOwnerId(pet.getCustomer().getId());
        petDTO.setNotes(pet.getPetNotes());
        petDTO.setType(pet.getPetType());

        return petDTO;
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {

        List<Pet> pets = petRepository.findAll();
        return pets
                .stream()
                .filter(pet -> pet.getCustomer().getId() == ownerId)
                .map(this::getDTO)
                .collect(Collectors.toList());
    }
}
