package com.udacity.jdnd.course3.critter.controller;

import com.udacity.jdnd.course3.critter.dto.ScheduleDTO;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.exception.ResourceNotFoundException;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import com.udacity.jdnd.course3.critter.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    UserService userService;

    @Autowired
    PetService petService;

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        return convertScheduleToScheduleDTO(this.scheduleService.saveSchedule(convertScheduleDTOToSchedule(scheduleDTO)));
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> schedules = this.scheduleService.getAllSchedules();
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        for(Schedule schedule:schedules){
            scheduleDTOS.add(convertScheduleToScheduleDTO(schedule));
        }
        return scheduleDTOS;
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        List<Schedule> schedules = this.scheduleService.getSchedulesByPetId(petId);
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        for(Schedule schedule:schedules){
            scheduleDTOS.add(convertScheduleToScheduleDTO(schedule));
        }
        return scheduleDTOS;
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        List<Schedule> schedules = this.scheduleService.getScheduleByEmployee(employeeId);
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        for(Schedule schedule:schedules){
            scheduleDTOS.add(convertScheduleToScheduleDTO(schedule));
        }
        return scheduleDTOS;
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        List<Schedule> schedules = this.scheduleService.getScheduleByOwner(customerId);
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        for(Schedule schedule:schedules){
            scheduleDTOS.add(convertScheduleToScheduleDTO(schedule));
        }
        return scheduleDTOS;
    }


    private ScheduleDTO convertScheduleToScheduleDTO(Schedule schedule) {

        ScheduleDTO scheduleDTO = new ScheduleDTO();
        BeanUtils.copyProperties(schedule, scheduleDTO);

        // setting the rest (pets, employees) of properties for schedulePTO
        scheduleDTO.setActivities(schedule.getActivities());

        List<Pet> pets = schedule.getPets();
        List<Long> petId = new ArrayList<>();
        for (Pet pet : pets) {
            petId.add(pet.getId());
        }
        scheduleDTO.setPetIds(petId);
        List<Employee> employees = schedule.getEmployees();
        List<Long> employeeId = new ArrayList<>();
        for (Employee employee : employees) {
            employeeId.add(employee.getId());
        }
        scheduleDTO.setEmployeeIds(employeeId);
        return scheduleDTO;

    }

    private Schedule convertScheduleDTOToSchedule(ScheduleDTO scheduleDTO){
        ModelMapper modelMapper = new ModelMapper();
        Schedule schedule = modelMapper.map(scheduleDTO, Schedule.class);

        // setting the rest (pets, employees) of properties for schedule
        schedule.setActivities(scheduleDTO.getActivities());
        HashMap<Long, Employee> employeeMap = new HashMap<>();
        for (Long employeeId : scheduleDTO.getEmployeeIds()) {
            Optional<Employee> optionalEmployee = Optional.ofNullable(userService.findEmployeeById(employeeId));
            if (optionalEmployee.isPresent()) {
                employeeMap.put(employeeId, optionalEmployee.get());
            } else {
                throw new ResourceNotFoundException();
            }
        }
        schedule.setEmployees(new ArrayList<Employee>(employeeMap.values()));
        HashMap<Long, Pet> petMap = new HashMap<>();
        for (Long petId : scheduleDTO.getPetIds()) {
            Optional<Pet> optionalPet = Optional.ofNullable(petService.getPetByPetId(petId));
            if (optionalPet.isPresent()) {
                petMap.put(petId, optionalPet.get());
            } else {
                throw new ResourceNotFoundException();
            }
        }
        schedule.setPets(new ArrayList<Pet>(petMap.values()));
        return schedule;
    }

}
