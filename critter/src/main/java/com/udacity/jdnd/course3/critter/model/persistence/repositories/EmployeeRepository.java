package com.udacity.jdnd.course3.critter.model.persistence.repositories;

import com.udacity.jdnd.course3.critter.model.persistence.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
