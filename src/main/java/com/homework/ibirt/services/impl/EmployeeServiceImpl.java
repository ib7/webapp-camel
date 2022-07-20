package com.homework.ibirt.services.impl;

import com.homework.ibirt.dto.EmployeeDto;
import com.homework.ibirt.exceptions.DepartmentNotFoundException;
import com.homework.ibirt.exceptions.EmployeeNotFoundException;
import com.homework.ibirt.jpa.models.Department;
import com.homework.ibirt.jpa.models.Employee;
import com.homework.ibirt.jpa.repositories.DepartmentRepository;
import com.homework.ibirt.jpa.repositories.EmployeeRepository;
import com.homework.ibirt.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    private final ConversionService conversionService;

    @Override
    public List<EmployeeDto> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        List<EmployeeDto> employeeDtoList = new ArrayList<>();

        for (Employee emp : employees) {
            employeeDtoList.add(conversionService.convert(emp, EmployeeDto.class));
        }

        return employeeDtoList;
    }

    @Override
    public EmployeeDto getEmployee(final UUID id) {
        final Employee employee = employeeRepository.findById(id).orElseThrow(() ->
                new EmployeeNotFoundException("Employee with id " + id + " was not found"));
        return conversionService.convert(employee, EmployeeDto.class);
    }

    @Override
    public EmployeeDto addEmployee(final EmployeeDto employeeDto) {
        final Employee employee = conversionService.convert(employeeDto, Employee.class);
        employee.setDepartment(searchForDepartment(employeeDto));
        return conversionService.convert(employeeRepository.save(employee), EmployeeDto.class);
    }

    @Override
    public EmployeeDto updateEmployee(final UUID id, final EmployeeDto employeeDto) {
        final Employee employee = employeeRepository.findById(id).orElseThrow(() ->
                new EmployeeNotFoundException("Employee with id " + id + " was not found"));

        if (nonNull(employeeDto.getFirstName())) {
            employee.setFirstName(employeeDto.getFirstName());
        }
        if (nonNull(employeeDto.getLastName())) {
            employee.setLastName(employeeDto.getLastName());
        }
        if (nonNull(employeeDto.getEmail())) {
            employee.setEmail(employeeDto.getEmail());
        }
        if (nonNull(employeeDto.getPhoneNumber())) {
            employee.setPhoneNumber(employeeDto.getPhoneNumber());
        }
        if (nonNull(employeeDto.getSalary())) {
            employee.setSalary(employeeDto.getSalary());
        }
        employee.setDepartment(searchForDepartment(employeeDto));

        employeeRepository.save(employee);

        return conversionService.convert(employee, EmployeeDto.class);
    }

    private Department searchForDepartment(final EmployeeDto employeeDto){
        Department department = null;

        if (nonNull(employeeDto.getDepartmentDto())) {
            final UUID depId = employeeDto.getDepartmentDto().getId();
            department = departmentRepository.findById(depId).orElseThrow(() ->
                    new DepartmentNotFoundException("Department with id " + depId + " was not found"));
        }
        return department;
    }
}