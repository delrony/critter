package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    CustomerService customerService;

    public ScheduleController(ScheduleService scheduleService, CustomerService customerService) {
        this.scheduleService = scheduleService;
        this.customerService = customerService;
    }

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();

        // Copy activities and date to the new schedule object
        BeanUtils.copyProperties(scheduleDTO, schedule);

        Schedule newSchedule = this.scheduleService.saveSchedule(schedule, scheduleDTO.getEmployeeIds(), scheduleDTO.getPetIds());

        ScheduleDTO newScheduleDTO = new ScheduleDTO();
        BeanUtils.copyProperties(newSchedule, newScheduleDTO);

        return this.copyEmplyeeAndPetIds(newSchedule, newScheduleDTO);

        //throw new UnsupportedOperationException();
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> schedules = this.scheduleService.getAllSchedules();

        List<ScheduleDTO> scheduleDTOList = new ArrayList<>();
        schedules.forEach(schedule -> {
            ScheduleDTO scheduleDTO = new ScheduleDTO();
            BeanUtils.copyProperties(schedule, scheduleDTO);

            scheduleDTO = this.copyEmplyeeAndPetIds(schedule, scheduleDTO);

            scheduleDTOList.add(scheduleDTO);
        });

        return scheduleDTOList;

        // throw new UnsupportedOperationException();
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        List<Schedule> schedules = this.scheduleService.getScheduleForPetId(petId);

        List<ScheduleDTO> scheduleDTOList = new ArrayList<>();
        schedules.forEach(schedule -> {
            ScheduleDTO scheduleDTO = new ScheduleDTO();
            BeanUtils.copyProperties(schedule, scheduleDTO);

            scheduleDTO = this.copyEmplyeeAndPetIds(schedule, scheduleDTO);

            scheduleDTOList.add(scheduleDTO);
        });

        return scheduleDTOList;

        //throw new UnsupportedOperationException();
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        List<Schedule> schedules = this.scheduleService.getScheduleForEmployee(employeeId);

        List<ScheduleDTO> scheduleDTOList = new ArrayList<>();
        schedules.forEach(schedule -> {
            ScheduleDTO scheduleDTO = new ScheduleDTO();
            BeanUtils.copyProperties(schedule, scheduleDTO);

            scheduleDTO = this.copyEmplyeeAndPetIds(schedule, scheduleDTO);

            scheduleDTOList.add(scheduleDTO);
        });

        return scheduleDTOList;
        //throw new UnsupportedOperationException();
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        Customer customer = this.customerService.getCustomer(customerId);
        List<Pet> customerPets = customer.getPets();

        List<Schedule> schedules = this.scheduleService.getScheduleForPets(customerPets);

        List<ScheduleDTO> scheduleDTOList = new ArrayList<>();
        schedules.forEach(schedule -> {
            ScheduleDTO scheduleDTO = new ScheduleDTO();
            BeanUtils.copyProperties(schedule, scheduleDTO);

            scheduleDTO = this.copyEmplyeeAndPetIds(schedule, scheduleDTO);

            scheduleDTOList.add(scheduleDTO);
        });

        return scheduleDTOList;

        //throw new UnsupportedOperationException();
    }

    private ScheduleDTO copyEmplyeeAndPetIds(Schedule from, ScheduleDTO to) {
        List<Long> employeeIds = new ArrayList<>();
        from.getEmployees().forEach(employee -> {
            employeeIds.add(employee.getId());
        });
        to.setEmployeeIds(employeeIds);

        List<Long> petIds = new ArrayList<>();
        from.getPets().forEach(pet -> {
            petIds.add(pet.getId());
        });
        to.setPetIds(petIds);

        return to;
    }
}
