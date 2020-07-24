package com.udacity.jdnd.course3.critter.model.persistence.repositories;

import com.udacity.jdnd.course3.critter.model.persistence.entities.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {
}
