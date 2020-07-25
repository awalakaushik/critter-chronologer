package com.udacity.jdnd.course3.critter.services;

import com.udacity.jdnd.course3.critter.dtos.CustomerDTO;
import com.udacity.jdnd.course3.critter.dtos.EmployeeDTO;
import com.udacity.jdnd.course3.critter.dtos.EmployeeRequestDTO;
import com.udacity.jdnd.course3.critter.model.persistence.entities.Customer;
import com.udacity.jdnd.course3.critter.model.persistence.entities.Employee;
import com.udacity.jdnd.course3.critter.model.persistence.entities.Pet;
import com.udacity.jdnd.course3.critter.model.persistence.repositories.CustomerRepository;
import com.udacity.jdnd.course3.critter.model.persistence.repositories.EmployeeRepository;
import com.udacity.jdnd.course3.critter.model.persistence.repositories.PetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    private CustomerRepository customerRepository;
    private EmployeeRepository employeeRepository;
    private PetRepository petRepository;

    public UserService(CustomerRepository customerRepository, EmployeeRepository employeeRepository,
                       PetRepository petRepository) {
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
        this.petRepository = petRepository;
    }

    public List<Employee> findEmployeesForService(EmployeeRequestDTO employeeDTO) {
        List<Employee> employees = employeeRepository.findAll();

        return employees.stream().filter(employee -> {
            List<DayOfWeek> availableDays = employee.getDaysAvailableForEmployee()
                    .stream()
                    .filter(dayOfWeek -> dayOfWeek == employeeDTO.getDate().getDayOfWeek())
                    .collect(Collectors.toList());
            return employee.getDaysAvailableForEmployee().containsAll(availableDays);
        }).collect(Collectors.toList())
                .stream()
                .filter(employee -> employee.getEmployeeSkills().containsAll(employeeDTO.getSkills()))
                .collect(Collectors.toList());
    }

    public void setAvailability(Set<DayOfWeek> daysAvailable, long employeeId) {
        Employee employee = employeeRepository.getOne(employeeId);
        employee.setDaysAvailableForEmployee(daysAvailable);
        employeeRepository.save(employee);
    }

    public Employee getEmployee(long employeeId) {
        return employeeRepository.getOne(employeeId);
    }

    public Employee saveEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();

        employee.setId(employeeDTO.getId());
        employee.setFullName(employeeDTO.getName());
        employee.setDaysAvailableForEmployee(employeeDTO.getDaysAvailable());

        if (employeeDTO.getSkills() != null && !employeeDTO.getSkills().isEmpty()) {
            employee.setEmployeeSkills(employeeDTO.getSkills());
        }

        return employeeRepository.save(employee);
    }

    public Customer getOwnerByPet(long petId) {
        return petRepository.getOne(petId).getCustomer();
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer saveCustomer(CustomerDTO customerDTO) {
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

        return customerRepository.save(customer);
    }
}
