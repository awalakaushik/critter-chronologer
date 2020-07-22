package com.udacity.jdnd.course3.critter.repositories;

import com.udacity.jdnd.course3.critter.models.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
