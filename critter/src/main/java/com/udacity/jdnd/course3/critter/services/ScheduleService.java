package com.udacity.jdnd.course3.critter.services;

import com.udacity.jdnd.course3.critter.dtos.ScheduleDTO;
import com.udacity.jdnd.course3.critter.models.entities.Customer;
import com.udacity.jdnd.course3.critter.models.entities.Employee;
import com.udacity.jdnd.course3.critter.models.entities.Pet;
import com.udacity.jdnd.course3.critter.models.entities.Schedule;
import com.udacity.jdnd.course3.critter.repositories.CustomerRepository;
import com.udacity.jdnd.course3.critter.repositories.EmployeeRepository;
import com.udacity.jdnd.course3.critter.repositories.PetRepository;
import com.udacity.jdnd.course3.critter.repositories.ScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ScheduleService {

    private CustomerRepository customerRepository;
    private EmployeeRepository employeeRepository;
    private ScheduleRepository scheduleRepository;
    private PetRepository petRepository;

    public ScheduleService(CustomerRepository customerRepository, EmployeeRepository employeeRepository,
                           ScheduleRepository scheduleRepository, PetRepository petRepository) {
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
        this.scheduleRepository = scheduleRepository;
        this.petRepository = petRepository;
    }

    public ScheduleDTO createSchedule(ScheduleDTO scheduleDTO) {

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

    public List<ScheduleDTO> getAllSchedules() {
        return scheduleRepository.findAll().stream().map(this::getDTO).collect(Collectors.toList());
    }

    public List<ScheduleDTO> getScheduleForPet(long petId) {
        Pet pet = petRepository.getOne(petId);

        return scheduleRepository.findAll()
                .stream()
                .filter(schedule -> schedule.getPetList().contains(pet))
                .collect(Collectors.toList())
                .stream()
                .map(this::getDTO)
                .collect(Collectors.toList());
    }

    public List<ScheduleDTO> getScheduleForEmployee(long employeeId) {
        Employee employee = employeeRepository.getOne(employeeId);

        return scheduleRepository.findAll()
                .stream()
                .filter(schedule -> schedule.getEmployeeList().contains(employee))
                .collect(Collectors.toList())
                .stream()
                .map(this::getDTO)
                .collect(Collectors.toList());
    }

    public List<ScheduleDTO> getScheduleForCustomer(long customerId) {
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
