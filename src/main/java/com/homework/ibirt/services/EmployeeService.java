package com.homework.ibirt.services;

import com.homework.ibirt.dto.EmployeeDto;
import java.util.List;
import java.util.UUID;

public interface EmployeeService {
    List<EmployeeDto> getAllEmployees();

    EmployeeDto getEmployee(UUID id);

    EmployeeDto addEmployee(EmployeeDto employeeDTO);

    EmployeeDto updateEmployee(UUID id, EmployeeDto employeeDTO);
}