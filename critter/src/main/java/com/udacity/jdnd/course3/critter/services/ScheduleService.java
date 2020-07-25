package com.udacity.jdnd.course3.critter.services;

import com.udacity.jdnd.course3.critter.dtos.ScheduleDTO;
import com.udacity.jdnd.course3.critter.model.persistence.entities.Customer;
import com.udacity.jdnd.course3.critter.model.persistence.entities.Employee;
import com.udacity.jdnd.course3.critter.model.persistence.entities.Pet;
import com.udacity.jdnd.course3.critter.model.persistence.entities.Schedule;
import com.udacity.jdnd.course3.critter.model.persistence.repositories.CustomerRepository;
import com.udacity.jdnd.course3.critter.model.persistence.repositories.EmployeeRepository;
import com.udacity.jdnd.course3.critter.model.persistence.repositories.PetRepository;
import com.udacity.jdnd.course3.critter.model.persistence.repositories.ScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ScheduleService {

    private ScheduleRepository scheduleRepository;
    private CustomerRepository customerRepository;
    private EmployeeRepository employeeRepository;
    private PetRepository petRepository;

    public ScheduleService(ScheduleRepository scheduleRepository, CustomerRepository customerRepository,
                           EmployeeRepository employeeRepository, PetRepository petRepository) {
        this.scheduleRepository = scheduleRepository;
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
        this.petRepository = petRepository;
    }


    public List<Schedule> getScheduleForCustomer(long customerId) {
        Customer customer = customerRepository.getOne(customerId);
        return scheduleRepository.findAll()
                .stream()
                .filter(schedule -> schedule.getPetList().containsAll(customer.getPets()))
                .collect(Collectors.toList());
    }

    public List<Schedule> getScheduleForEmployee(long employeeId) {
        Employee employee = employeeRepository.getOne(employeeId);
        return scheduleRepository.findAll()
                .stream()
                .filter(schedule -> schedule.getEmployeeList().contains(employee))
                .collect(Collectors.toList());
    }

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public List<Schedule> getScheduleForPet(long petId) {
        Pet pet = petRepository.getOne(petId);
        return scheduleRepository.findAll()
                .stream()
                .filter(schedule -> schedule.getPetList().contains(pet))
                .collect(Collectors.toList());
    }

    public Schedule createSchedule(ScheduleDTO scheduleDTO) {
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

        return scheduleRepository.save(schedule);
    }
}
