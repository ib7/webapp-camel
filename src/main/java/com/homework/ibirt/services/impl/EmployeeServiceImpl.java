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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.homework.ibirt.dto.EmployeeDto.fromEmployee;
import static com.homework.ibirt.dto.EmployeeDto.toEmployee;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    public List<EmployeeDto> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(EmployeeDto::fromEmployee)
                .collect(toList());
    }

    @Override
    public EmployeeDto getEmployee(final UUID id) {
        final Employee employee = employeeRepository.findById(id).orElseThrow(() ->
                new EmployeeNotFoundException("Employee with id " + id + " was not found"));
        return fromEmployee(employee);
    }

    @Override
    public EmployeeDto addEmployee(final EmployeeDto employeeDto) {
        final Employee employee = toEmployee(employeeDto);
        employee.setDepartment(searchForDepartment(employeeDto));
        return fromEmployee(employeeRepository.save(employee));
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

        return fromEmployee(employee);
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