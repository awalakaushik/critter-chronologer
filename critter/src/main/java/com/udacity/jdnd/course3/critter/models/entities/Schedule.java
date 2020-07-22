package com.udacity.jdnd.course3.critter.models.entities;

import com.udacity.jdnd.course3.critter.models.entities.Employee;
import com.udacity.jdnd.course3.critter.models.entities.Pet;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table
public class Schedule {

    public Schedule() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToMany(targetEntity = Employee.class)
    private List<Employee> employeeList;

    @ManyToMany(targetEntity = Pet.class)
    private List<Pet> petList;

    @ElementCollection
    private Set<EmployeeSkill> employeeSkills;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    public List<Pet> getPetList() {
        return petList;
    }

    public void setPetList(List<Pet> petList) {
        this.petList = petList;
    }

    public Set<EmployeeSkill> getEmployeeSkills() {
        return employeeSkills;
    }

    public void setEmployeeSkills(Set<EmployeeSkill> employeeSkills) {
        this.employeeSkills = employeeSkills;
    }
}
