package com.homework.ibirt.dto.converters;

import com.homework.ibirt.dto.EmployeeDto;
import com.homework.ibirt.jpa.models.Department;
import com.homework.ibirt.jpa.models.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import static java.util.Objects.nonNull;

@Component
@RequiredArgsConstructor
public class EmployeeDtoToEmployeeConverter implements Converter<EmployeeDto, Employee> {

    private final ConversionService conversionService;

    @Override
    public Employee convert(EmployeeDto source) {
        Employee employee = Employee
                .builder()
                .firstName(source.getFirstName())
                .lastName(source.getLastName())
                .email(source.getEmail())
                .phoneNumber(source.getPhoneNumber())
                .salary(source.getSalary())
                .build();
        if (nonNull(source.getDepartmentDto())) {
            employee.setDepartment(conversionService.convert(source.getDepartmentDto(), Department.class));
        }
        return employee;
    }
}