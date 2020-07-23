package com.udacity.jdnd.course3.critter.models.entities;

import com.udacity.jdnd.course3.critter.dtos.CustomerDTO;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table
public class Customer {

    public Customer() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 50)
    private String fullName;

    @Column(length = 15)
    private String customerPhoneNumber;

    @Column(length = 1000)
    private String customerNotes;

    @OneToMany(targetEntity = Pet.class, fetch = FetchType.LAZY)
    private List<Pet> pets;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    public String getCustomerNotes() {
        return customerNotes;
    }

    public void setCustomerNotes(String customerNotes) {
        this.customerNotes = customerNotes;
    }

    public List<Pet> getPets() {
        return pets;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }

    public void addPet(Pet pet) {
        pets.add(pet);
    }

}
