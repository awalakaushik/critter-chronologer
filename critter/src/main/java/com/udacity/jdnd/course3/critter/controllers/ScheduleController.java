package com.udacity.jdnd.course3.critter.controllers;

import com.udacity.jdnd.course3.critter.dtos.ScheduleDTO;
import com.udacity.jdnd.course3.critter.model.persistence.entities.Employee;
import com.udacity.jdnd.course3.critter.model.persistence.entities.Pet;
import com.udacity.jdnd.course3.critter.model.persistence.entities.Schedule;
import com.udacity.jdnd.course3.critter.services.PetService;
import com.udacity.jdnd.course3.critter.services.ScheduleService;
import com.udacity.jdnd.course3.critter.services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    private UserService userService;
    private ScheduleService scheduleService;
    private PetService petService;

    public ScheduleController(UserService userService, ScheduleService scheduleService, PetService petService) {
        this.userService = userService;
        this.scheduleService = scheduleService;
        this.petService = petService;
    }

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        return getDTO(scheduleService.createSchedule(scheduleDTO));
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        return scheduleService.getAllSchedules().stream().map(this::getDTO).collect(Collectors.toList());
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        return scheduleService.getScheduleForPet(petId)
                .stream()
                .map(this::getDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        return scheduleService.getScheduleForEmployee(employeeId)
                .stream()
                .map(this::getDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        return scheduleService.getScheduleForCustomer(customerId)
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
