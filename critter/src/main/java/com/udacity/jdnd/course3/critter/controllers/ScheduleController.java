package com.udacity.jdnd.course3.critter.controllers;

import com.udacity.jdnd.course3.critter.dtos.ScheduleDTO;
import com.udacity.jdnd.course3.critter.model.persistence.entities.Customer;
import com.udacity.jdnd.course3.critter.model.persistence.entities.Employee;
import com.udacity.jdnd.course3.critter.model.persistence.entities.Pet;
import com.udacity.jdnd.course3.critter.model.persistence.entities.Schedule;
import com.udacity.jdnd.course3.critter.model.persistence.repositories.CustomerRepository;
import com.udacity.jdnd.course3.critter.model.persistence.repositories.EmployeeRepository;
import com.udacity.jdnd.course3.critter.model.persistence.repositories.PetRepository;
import com.udacity.jdnd.course3.critter.model.persistence.repositories.ScheduleRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    private CustomerRepository customerRepository;
    private EmployeeRepository employeeRepository;
    private ScheduleRepository scheduleRepository;
    private PetRepository petRepository;

    public ScheduleController(CustomerRepository customerRepository, EmployeeRepository employeeRepository,
                              ScheduleRepository scheduleRepository, PetRepository petRepository) {
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
        this.scheduleRepository = scheduleRepository;
        this.petRepository = petRepository;
    }

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();

        schedule.setEmployeeList(employeeRepository
                .findAll()
                .stream()
                .filter(employee -> scheduleDTO.getEmployeeIds().contains(employee.getId()))
                .collect(Collectors.toList()));
        schedule.setEmployeeSkills(scheduleDTO.getActivities());
        schedule.setId(scheduleDTO.getId());
        schedule.setPetList(petRepository.findAll()
                .stream()
                .filter(pet -> scheduleDTO.getPetIds().contains(pet.getId()))
                .collect(Collectors.toList()));
        schedule.setDate(scheduleDTO.getDate());

        Schedule savedSchedule = scheduleRepository.save(schedule);

        return getDTO(savedSchedule);
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {

        return scheduleRepository.findAll().stream().map(this::getDTO).collect(Collectors.toList());
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        Pet pet = petRepository.getOne(petId);

        return scheduleRepository.findAll()
                .stream()
                .filter(schedule -> schedule.getPetList().contains(pet))
                .collect(Collectors.toList())
                .stream()
                .map(this::getDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        Employee employee = employeeRepository.getOne(employeeId);

        return scheduleRepository.findAll()
                .stream()
                .filter(schedule -> schedule.getEmployeeList().contains(employee))
                .collect(Collectors.toList())
                .stream()
                .map(this::getDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        Customer customer = customerRepository.getOne(customerId);

        return scheduleRepository.findAll()
                .stream()
                .filter(schedule -> schedule.getPetList().containsAll(customer.getPets()))
                .collect(Collectors.toList())
                .stream()
                .map(this::getDTO)
                .collect(Collectors.toList());
    }

    private ScheduleDTO getDTO(Schedule schedule) {

        ScheduleDTO scheduleDTO = new ScheduleDTO();

        scheduleDTO.setId(schedule.getId());
        scheduleDTO.setPetIds(schedule.getPetList().stream().map(Pet::getId).collect(Collectors.toList()));
        scheduleDTO.setActivities(schedule.getEmployeeSkills());
        scheduleDTO.setEmployeeIds(schedule.getEmployeeList().stream().map(Employee::getId).collect(Collectors.toList()));
        scheduleDTO.setDate(schedule.getDate());

        return scheduleDTO;
    }
}
