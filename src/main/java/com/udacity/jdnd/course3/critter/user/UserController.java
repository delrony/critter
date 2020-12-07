package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
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
    CustomerService customerService;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    PetService petService;

    public UserController(CustomerService customerService, EmployeeService employeeService, PetService petService) {
        this.customerService = customerService;
        this.employeeService = employeeService;
        this.petService = petService;
    }

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer, "id");

        customer = this.customerService.saveCustomer(customer);

        CustomerDTO newCustomerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, newCustomerDTO);
        return newCustomerDTO;
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        List<Customer> customers = this.customerService.getAllCustomers();

        List<CustomerDTO> customerDTOList = new ArrayList<>();
        customers.forEach(customer -> {
            customerDTOList.add(this.getDTOFromCustomer(customer));
        });

        return customerDTOList;
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        Pet pet = this.petService.getPet(petId);
        Customer customer = pet.getCustomer();

        return this.getDTOFromCustomer(customer);
    }

    private CustomerDTO getDTOFromCustomer(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        if (customer != null) {
            BeanUtils.copyProperties(customer, customerDTO);

            List<Pet> customerPets = customer.getPets();
            if (customerPets != null) {
                List<Long> petIds = new ArrayList<>();
                customerPets.forEach(customerPet -> {
                    petIds.add(customerPet.getId());
                });
                customerDTO.setPetIds(petIds);
            }
        } else {
            throw new CustomerNotFoundException();
        }

        return customerDTO;
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee, "id");

        employee = this.employeeService.saveEmployee(employee);

        EmployeeDTO newEmployeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee, newEmployeeDTO);
        return newEmployeeDTO;

        //throw new UnsupportedOperationException();
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        Employee employee = this.employeeService.getEmployee(employeeId);

        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee, employeeDTO);

        return employeeDTO;

        //throw new UnsupportedOperationException();
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        this.employeeService.saveAvailability(daysAvailable, employeeId);

        //throw new UnsupportedOperationException();
    }

    @PostMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {

        List<Employee> employees = this.employeeService.findEmployeeByDateAndSkills(employeeDTO.getDate(), employeeDTO.getSkills());

        List<EmployeeDTO> employeeDTOList = new ArrayList<>();
        employees.forEach(employee -> {
            EmployeeDTO employeeDTOResult = new EmployeeDTO();
            BeanUtils.copyProperties(employee, employeeDTOResult);

            employeeDTOList.add(employeeDTOResult);
        });

        return employeeDTOList;
        //throw new UnsupportedOperationException();
    }

}
