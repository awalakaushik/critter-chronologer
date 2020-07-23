package com.udacity.jdnd.course3.critter.services;

import com.udacity.jdnd.course3.critter.dtos.PetDTO;
import com.udacity.jdnd.course3.critter.models.entities.Customer;
import com.udacity.jdnd.course3.critter.models.entities.Pet;
import com.udacity.jdnd.course3.critter.repositories.CustomerRepository;
import com.udacity.jdnd.course3.critter.repositories.PetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PetService {

    private PetRepository petRepository;
    private CustomerRepository customerRepository;

    public PetService(PetRepository petRepository, CustomerRepository customerRepository) {
        this.petRepository = petRepository;
        this.customerRepository = customerRepository;
    }

    public PetDTO savePet(PetDTO petDTO) {
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


    public List<PetDTO> getPetsByOwner(long ownerId) {
        List<Pet> pets = petRepository.findAll();
        return pets
                .stream()
                .filter(pet -> pet.getCustomer().getId() == ownerId)
                .map(this::getDTO)
                .collect(Collectors.toList());
    }

    public PetDTO getPet(long petId) {
        Pet pet = petRepository.getOne(petId);
        return getDTO(pet);
    }
}
