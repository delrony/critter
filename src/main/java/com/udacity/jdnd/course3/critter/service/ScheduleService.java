package com.udacity.jdnd.course3.critter.service;

import com.google.common.collect.Lists;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.repository.SchedultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduleService {
    @Autowired
    SchedultRepository schedultRepository;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    PetService petService;

    public ScheduleService(SchedultRepository schedultRepository, EmployeeService employeeService, PetService petService) {
        this.schedultRepository = schedultRepository;
        this.employeeService = employeeService;
        this.petService = petService;
    }

    public Schedule saveSchedule(Schedule schedule, List<Long> employeeIds, List<Long> petIds) {
        List<Employee> employees = new ArrayList<>();
        employeeIds.forEach(employeeId -> {
            Employee employee = this.employeeService.getEmployee(employeeId);
            employees.add(employee);
        });
        schedule.setEmployees(employees);

        List<Pet> pets = new ArrayList<>();
        petIds.forEach(petId -> {
            Pet pet = this.petService.getPet(petId);
            pets.add(pet);
        });
        schedule.setPets(pets);

        return this.schedultRepository.save(schedule);
    }

    public List<Schedule> getAllSchedules() {
        return this.schedultRepository.findAll();
    }

    public List<Schedule> getScheduleForEmployee(Long employeeId) {
        Employee employee = this.employeeService.getEmployee(employeeId);
        return this.schedultRepository.findAllByEmployees(employee);
    }

    public List<Schedule> getScheduleForPetId(Long petId) {
        Pet pet = this.petService.getPet(petId);

        List<Pet> pets = Lists.newArrayList(pet);

        return this.schedultRepository.findAllByPetsIn(pets);
    }

    public List<Schedule> getScheduleForPets(List<Pet> pets) {
        return this.schedultRepository.findAllByPetsIn(pets);
    }
}
