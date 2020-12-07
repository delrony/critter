package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.user.EmployeeNotFoundException;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public Employee saveEmployee(Employee employee) {
        employee = this.employeeRepository.save(employee);

        return employee;
    }

    public Employee getEmployee(Long employeeId) {
        Optional<Employee> optionalEmployee = this.employeeRepository.findById(employeeId);
        if (!optionalEmployee.isPresent()) {
            throw new EmployeeNotFoundException("Employee not found for ID " + employeeId);
        }

        return optionalEmployee.get();
    }

    @Transactional
    public void saveAvailability(Set<DayOfWeek> daysAvailable, Long employeeId) {
        Employee employee = this.getEmployee(employeeId);

        employee.setDaysAvailable(daysAvailable);
        this.employeeRepository.save(employee);
    }

    public List<Employee> findEmployeeByDateAndSkills(LocalDate date, Set<EmployeeSkill> skills) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();

        List<Employee> employees = this.employeeRepository.findDistinctByDaysAvailableAndSkillsIn(dayOfWeek, skills);

        List<Employee> resultEmployees = new ArrayList<>();

        employees.forEach(employee -> {
            boolean isAllSkillsAvailable = true;
            for (EmployeeSkill skill: skills) {
                if (!employee.getSkills().contains(skill)) {
                    isAllSkillsAvailable = false;
                    break;
                }
            }

            if (isAllSkillsAvailable) {
                resultEmployees.add(employee);
            }

        });

        return resultEmployees;
    }
}
