package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

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
        BeanUtils.copyProperties(scheduleDTO, schedule, "id");

        Schedule newSchedule = this.scheduleService.saveSchedule(schedule, scheduleDTO.getEmployeeIds(), scheduleDTO.getPetIds());

        return this.getDTOFromSchedule(newSchedule);
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> schedules = this.scheduleService.getAllSchedules();

        return this.getDTOListFromSchedules(schedules);
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        List<Schedule> schedules = this.scheduleService.getScheduleForPetId(petId);

        return this.getDTOListFromSchedules(schedules);
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        List<Schedule> schedules = this.scheduleService.getScheduleForEmployee(employeeId);

        return this.getDTOListFromSchedules(schedules);
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        Customer customer = this.customerService.getCustomer(customerId);
        List<Pet> customerPets = customer.getPets();

        List<Schedule> schedules = this.scheduleService.getScheduleForPets(customerPets);

        return this.getDTOListFromSchedules(schedules);
    }

    private List<ScheduleDTO> getDTOListFromSchedules(List<Schedule> schedules) {
        List<ScheduleDTO> scheduleDTOList = new ArrayList<>();
        schedules.forEach(schedule -> {
            scheduleDTOList.add(this.getDTOFromSchedule(schedule));
        });

        return scheduleDTOList;
    }

    private ScheduleDTO getDTOFromSchedule(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        BeanUtils.copyProperties(schedule, scheduleDTO);

        this.copyEmpoyeesFromSchedule(schedule, scheduleDTO);
        this.copyPetsFromSchedule(schedule, scheduleDTO);

        return scheduleDTO;
    }

    private void copyEmpoyeesFromSchedule(Schedule from, ScheduleDTO to) {
        List<Long> employeeIds = new ArrayList<>();
        from.getEmployees().forEach(employee -> {
            employeeIds.add(employee.getId());
        });
        to.setEmployeeIds(employeeIds);
    }

    private void copyPetsFromSchedule(Schedule from, ScheduleDTO to) {
        List<Long> petIds = new ArrayList<>();
        from.getPets().forEach(pet -> {
            petIds.add(pet.getId());
        });
        to.setPetIds(petIds);
    }
}
