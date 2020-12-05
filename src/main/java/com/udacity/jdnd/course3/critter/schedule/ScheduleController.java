package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.entity.Schedule;
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

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();

        // Copy activities and date to the new schedule object
        BeanUtils.copyProperties(scheduleDTO, schedule);

        Schedule newSchedule = this.scheduleService.saveSchedule(schedule, scheduleDTO.getEmployeeIds(), scheduleDTO.getPetIds());

        ScheduleDTO newScheduleDTO = new ScheduleDTO();
        BeanUtils.copyProperties(newSchedule, newScheduleDTO);

        List<Long> employeeIds = new ArrayList<>();
        newSchedule.getEmployees().forEach(employee -> {
            employeeIds.add(employee.getId());
        });
        newScheduleDTO.setEmployeeIds(employeeIds);

        List<Long> petIds = new ArrayList<>();
        newSchedule.getPets().forEach(pet -> {
            petIds.add(pet.getId());
        });
        newScheduleDTO.setPetIds(petIds);

        return newScheduleDTO;

        //throw new UnsupportedOperationException();
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> schedules = this.scheduleService.getAllSchedules();

        List<ScheduleDTO> scheduleDTOList = new ArrayList<>();
        schedules.forEach(schedule -> {
            ScheduleDTO scheduleDTO = new ScheduleDTO();
            BeanUtils.copyProperties(schedule, scheduleDTO);

            List<Long> employeeIds = new ArrayList<>();
            schedule.getEmployees().forEach(employee -> {
                employeeIds.add(employee.getId());
            });
            scheduleDTO.setEmployeeIds(employeeIds);

            List<Long> petIds = new ArrayList<>();
            schedule.getPets().forEach(pet -> {
                petIds.add(pet.getId());
            });
            scheduleDTO.setPetIds(petIds);

            scheduleDTOList.add(scheduleDTO);
        });

        return scheduleDTOList;

        // throw new UnsupportedOperationException();
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        throw new UnsupportedOperationException();
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        throw new UnsupportedOperationException();
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        throw new UnsupportedOperationException();
    }
}
