package com.udacity.jdnd.course3.critter.services;

import com.udacity.jdnd.course3.critter.dtos.CustomerDTO;
import com.udacity.jdnd.course3.critter.dtos.EmployeeDTO;
import com.udacity.jdnd.course3.critter.models.entities.Customer;
import com.udacity.jdnd.course3.critter.models.entities.Employee;
import com.udacity.jdnd.course3.critter.models.entities.Pet;
import com.udacity.jdnd.course3.critter.repositories.EmployeeRepository;
import com.udacity.jdnd.course3.critter.repositories.PetRepository;
import com.udacity.jdnd.course3.critter.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private CustomerRepository customerRepository;
    private EmployeeRepository employeeRepository;
    private PetRepository petRepository;

    public UserService(CustomerRepository customerRepository, EmployeeRepository employeeRepository, PetRepository petRepository) {
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
        this.petRepository = petRepository;
    }

    public CustomerDTO saveCustomer(CustomerDTO customerDto) {
        Customer customer = new Customer();

        customer.setId(customerDto.getId());
        customer.setFullName(customerDto.getName());
        customer.setCustomerNotes(customerDto.getNotes());
        customer.setCustomerPhoneNumber(customerDto.getPhoneNumber());

        if (customerDto.getPetIds() != null && !customerDto.getPetIds().isEmpty()) {

            List<Pet> customerPets = customerDto
                    .getPetIds()
                    .stream()
                    .map((Long id) -> petRepository.getOne(id))
                    .collect(Collectors.toList());
            customer.setPets(customerPets);
        }

        Customer savedCustomer = customerRepository.save(customer);

        return getDTO(savedCustomer);
    }

    public EmployeeDTO saveEmployee(EmployeeDTO employeeDTO) {
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

    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream().map(customer -> getDTO(customer)).collect(Collectors.toList());
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

    public EmployeeDTO getEmployee(long employeeId) {
        Employee employee = employeeRepository.getOne(employeeId);
        return getDTO(employee);
    }

    public CustomerDTO getOwnerByPet(long petId) {
        return getDTO(petRepository.getOne(petId).getCustomer());
    }
}
