package com.udacity.jdnd.course3.critter.controller;

import com.udacity.jdnd.course3.critter.dto.CustomerDTO;
import com.udacity.jdnd.course3.critter.dto.EmployeeDTO;
import com.udacity.jdnd.course3.critter.dto.EmployeeRequestDTO;
import com.udacity.jdnd.course3.critter.entity.*;
import com.udacity.jdnd.course3.critter.exception.BadRequestException;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    PetService petService;

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        List<Long> petIds = customerDTO.getPetIds();
        List<Pet> pets = new ArrayList<>();

        if (petIds != null) {
            for (Long petId : petIds) {
                pets.add(petService.getPetByPetId(petId));
            }
        }

        Owner owner = convertCustomerDTOToOwner(customerDTO);
        owner.setPets(pets);
        Owner savedOwner = userService.saveCustomer(owner);
        return convertOwnerToCustomerDTO(savedOwner);

    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        List<Owner> owners = this.userService.findAllOwners();
        List<CustomerDTO> customerDTOS = new ArrayList<>();
        for(Owner owner : owners){
            customerDTOS.add(convertOwnerToCustomerDTO(owner));
        }
        return customerDTOS;
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        Pet pet = this.petService.getPetByPetId(petId);
        if(pet.getOwner() != null){
            return convertOwnerToCustomerDTO(pet.getOwner());
        }
        else throw new UnsupportedOperationException("Apparently the Pet doesn't have an Owner yet!");
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee givenEmployee = convertEmployeeDTOToEmployee(employeeDTO);
        Employee savedEmployee = userService.saveEmployee(givenEmployee);
        employeeDTO.setId(savedEmployee.getId());
        return employeeDTO;
    }

    @GetMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        return convertEmployeeToEmployeeDTO(this.userService.findEmployeeById(employeeId));
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        this.userService.updateDaysAvailable(daysAvailable, employeeId);
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) throws BadRequestException {
        List<Employee> employees = this.userService.findEmployeesForService(employeeDTO.getSkills(),employeeDTO.getDate());
        List<EmployeeDTO> employeeDTOS = new ArrayList<>();

        for (Employee employee : employees) {
            employeeDTOS.add(convertEmployeeToEmployeeDTO(employee));
        }
        return employeeDTOS;
    }

    private CustomerDTO convertOwnerToCustomerDTO(Owner owner){
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(owner, customerDTO);
        List<Pet> pets = owner.getPets();

        if (pets != null) {
            List<Long> petIds = new ArrayList<>();

            for (Pet pet : pets) {
                petIds.add(pet.getId());
            }
            customerDTO.setPetIds(petIds);
        }
        return customerDTO;
    }

    private Owner convertCustomerDTOToOwner(CustomerDTO customerDTO){
        ModelMapper modelMapper = new ModelMapper();
        Owner owner = modelMapper.map(customerDTO, Owner.class);
        List<Long> petIds = customerDTO.getPetIds();

        if (petIds != null) {
            List<Pet> pets = new ArrayList<Pet>();

            for (Long petId : petIds) {
                pets.add(petService.getPetByPetId(petId));
            }
            owner.setPets(pets);
        }
        return owner;
    }

    private EmployeeDTO convertEmployeeToEmployeeDTO(Employee employee){
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee, employeeDTO);
        return employeeDTO;
    }

    private Employee convertEmployeeDTOToEmployee(EmployeeDTO employeeDTO){
        ModelMapper modelMapper = new ModelMapper();
        Employee employee = modelMapper.map(employeeDTO, Employee.class);
        return employee;
    }

}
