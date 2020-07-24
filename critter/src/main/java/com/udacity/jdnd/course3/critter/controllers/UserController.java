package com.udacity.jdnd.course3.critter.controllers;

import com.udacity.jdnd.course3.critter.dtos.CustomerDTO;
import com.udacity.jdnd.course3.critter.dtos.EmployeeDTO;
import com.udacity.jdnd.course3.critter.dtos.EmployeeRequestDTO;
import com.udacity.jdnd.course3.critter.model.persistence.entities.Customer;
import com.udacity.jdnd.course3.critter.model.persistence.entities.Employee;
import com.udacity.jdnd.course3.critter.model.persistence.entities.Pet;
import com.udacity.jdnd.course3.critter.model.persistence.repositories.CustomerRepository;
import com.udacity.jdnd.course3.critter.model.persistence.repositories.EmployeeRepository;
import com.udacity.jdnd.course3.critter.model.persistence.repositories.PetRepository;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private CustomerRepository customerRepository;
    private EmployeeRepository employeeRepository;
    private PetRepository petRepository;

    public UserController(CustomerRepository customerRepository, EmployeeRepository employeeRepository,
                          PetRepository petRepository) {
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
        this.petRepository = petRepository;
    }

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        Customer customer = new Customer();

        customer.setId(customerDTO.getId());
        customer.setFullName(customerDTO.getName());
        customer.setCustomerNotes(customerDTO.getNotes());
        customer.setCustomerPhoneNumber(customerDTO.getPhoneNumber());

        if (customerDTO.getPetIds() != null && !customerDTO.getPetIds().isEmpty()) {

            List<Pet> customerPets = customerDTO
                    .getPetIds()
                    .stream()
                    .map((Long id) -> petRepository.getOne(id))
                    .collect(Collectors.toList());
            customer.setPets(customerPets);
        }

        Customer savedCustomer = customerRepository.save(customer);

        return getDTO(savedCustomer);
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){

        List<Customer> customers = customerRepository.findAll();
        return customers.stream().map(this::getDTO).collect(Collectors.toList());
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        return getDTO(petRepository.getOne(petId).getCustomer());
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = new Employee();

        employee.setId(employeeDTO.getId());
        employee.setFullName(employeeDTO.getName());
        employee.setDaysAvailableForEmployee(employeeDTO.getDaysAvailable());

        if (employeeDTO.getSkills() != null && !employeeDTO.getSkills().isEmpty()) {
            employee.setEmployeeSkills(employeeDTO.getSkills());
        }

        Employee savedEmployee = employeeRepository.save(employee);

        return getDTO(savedEmployee);
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {

        Employee employee = employeeRepository.getOne(employeeId);
        return getDTO(employee);
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        Employee employee = employeeRepository.getOne(employeeId);
        employee.setDaysAvailableForEmployee(daysAvailable);
        employeeRepository.save(employee);
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        List<Employee> employees = employeeRepository.findAll();

        List<Employee> filteredEmployees = employees.stream().filter(employee -> {
            List<DayOfWeek> availableDays = employee.getDaysAvailableForEmployee()
                    .stream()
                    .filter(dayOfWeek -> dayOfWeek == employeeDTO.getDate().getDayOfWeek())
                    .collect(Collectors.toList());
            return employee.getDaysAvailableForEmployee().containsAll(availableDays);
        }).collect(Collectors.toList())
                .stream()
                .filter(employee -> employee.getEmployeeSkills().containsAll(employeeDTO.getSkills()))
                .collect(Collectors.toList());

        return filteredEmployees
                .stream()
                .map(this::getDTO)
                .collect(Collectors.toList());
    }

    private CustomerDTO getDTO(Customer customer) {

        CustomerDTO customerDTO = new CustomerDTO();

        customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getFullName());
        customerDTO.setNotes(customer.getCustomerNotes());
        customerDTO.setPhoneNumber(customer.getCustomerPhoneNumber());

        if (customer.getPets() != null && !customer.getPets().isEmpty()) {
            List<Long> petIds = customer.getPets().stream().map(Pet::getId).collect(Collectors.toList());
            customerDTO.setPetIds(petIds);
        }

        return customerDTO;
    }

    private EmployeeDTO getDTO(Employee employee) {

        EmployeeDTO employeeDTO = new EmployeeDTO();

        employeeDTO.setId(employee.getId());
        employeeDTO.setName(employee.getFullName());
        employeeDTO.setDaysAvailable(employee.getDaysAvailableForEmployee());

        if (employee.getEmployeeSkills() != null && !employee.getEmployeeSkills().isEmpty()) {
            employeeDTO.setSkills(employee.getEmployeeSkills());
        }

        return employeeDTO;
    }
}
