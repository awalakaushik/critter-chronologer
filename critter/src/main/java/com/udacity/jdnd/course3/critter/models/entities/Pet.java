package com.udacity.jdnd.course3.critter.models.entities;

import com.udacity.jdnd.course3.critter.enums.PetType;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table
public class Pet {
    public Pet() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(targetEntity = Customer.class, optional = false)
    private Customer customer;

    private PetType petType;

    private String petName;

    private LocalDate petBirthDate;

    private String petNotes;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public PetType getPetType() {
        return petType;
    }

    public void setPetType(PetType petType) {
        this.petType = petType;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public LocalDate getPetBirthDate() {
        return petBirthDate;
    }

    public void setPetBirthDate(LocalDate petBirthDate) {
        this.petBirthDate = petBirthDate;
    }

    public String getPetNotes() {
        return petNotes;
    }

    public void setPetNotes(String petNotes) {
        this.petNotes = petNotes;
    }
}
