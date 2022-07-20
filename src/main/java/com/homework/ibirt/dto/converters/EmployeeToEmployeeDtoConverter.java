package com.homework.ibirt.dto.converters;

import com.homework.ibirt.dto.DepartmentDto;
import com.homework.ibirt.dto.EmployeeDto;
import com.homework.ibirt.jpa.models.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import static java.util.Objects.nonNull;

@Component
@RequiredArgsConstructor
public class EmployeeToEmployeeDtoConverter implements Converter<Employee, EmployeeDto> {

    private final ConversionService conversionService;

    @Override
    public EmployeeDto convert(Employee source) {
        EmployeeDto employeeDto = EmployeeDto
                .builder()
                .id(source.getId())
                .firstName(source.getFirstName())
                .lastName(source.getLastName())
                .email(source.getEmail())
                .phoneNumber(source.getPhoneNumber())
                .salary(source.getSalary())
                .build();
        if (nonNull(source.getDepartment())) {
            employeeDto.setDepartmentDto(conversionService.convert(source.getDepartment(), DepartmentDto.class));
        }
        return employeeDto;
    }
}