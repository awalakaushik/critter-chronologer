package com.udacity.jdnd.course3.critter.services;

import com.udacity.jdnd.course3.critter.dtos.PetDTO;
import com.udacity.jdnd.course3.critter.model.persistence.entities.Customer;
import com.udacity.jdnd.course3.critter.model.persistence.entities.Pet;
import com.udacity.jdnd.course3.critter.model.persistence.repositories.CustomerRepository;
import com.udacity.jdnd.course3.critter.model.persistence.repositories.PetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class PetService {

    private PetRepository petRepository;
    private CustomerRepository customerRepository;

    public PetService(PetRepository petRepository, CustomerRepository customerRepository) {
        this.petRepository = petRepository;
        this.customerRepository = customerRepository;
    }

    public List<Pet> getPetsByOwner() {
        return petRepository.findAll();
    }

    public List<Pet> getPets() {
        return petRepository.findAll();
    }

    public Pet getPet(long petId) {
        return petRepository.getOne(petId);
    }

    public Pet savePet(PetDTO petDTO) {
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

        return savedPet;
    }
}
