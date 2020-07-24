package com.udacity.jdnd.course3.critter.model.persistence.repositories;

import com.udacity.jdnd.course3.critter.model.persistence.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
