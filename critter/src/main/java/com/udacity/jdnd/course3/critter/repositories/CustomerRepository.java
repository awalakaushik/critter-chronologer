package com.udacity.jdnd.course3.critter.repositories;

import com.udacity.jdnd.course3.critter.models.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
