package com.homework.ibirt.dto;

import com.homework.ibirt.jpa.models.Employee;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.UUID;

import static com.homework.ibirt.dto.DepartmentDto.fromDepartment;
import static com.homework.ibirt.dto.DepartmentDto.toDepartment;
import static java.util.Objects.nonNull;

@Data
@Builder
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
    private UUID id;

    @NotBlank(message = "Employee first name should not be empty/blank/null")
    private String firstName;

    @NotBlank(message = "Employee last name should not be empty/blank/null")
    private String lastName;

    private DepartmentDto departmentDto;

    @NotBlank(message = "Employee email should not be empty/blank/null")
    @Email(message = "Email should be a valid email")
    private String email;

    @NotBlank(message = "Employee phone number should not be empty/blank/null")
    private String phoneNumber;

    @Min(value = 1, message = "Employee salary should be greater then 1")
    private BigDecimal salary;

    public static EmployeeDto fromEmployee(final Employee employee) {
        EmployeeDto employeeDto = EmployeeDto.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .phoneNumber(employee.getPhoneNumber())
                .salary(employee.getSalary())
                .build();
        if (nonNull(employee.getDepartment())) {
            employeeDto.setDepartmentDto(fromDepartment(employee.getDepartment()));
        }
        return employeeDto;
    }

    public static Employee toEmployee(final EmployeeDto employeeDto) {

        Employee employee = Employee.builder()
                .firstName(employeeDto.getFirstName())
                .lastName(employeeDto.getLastName())
                .email(employeeDto.getEmail())
                .phoneNumber(employeeDto.getPhoneNumber())
                .salary(employeeDto.getSalary())
                .build();
        if (nonNull(employeeDto.getDepartmentDto())) {
            employee.setDepartment(toDepartment(employeeDto.getDepartmentDto()));
        }
        return employee;
    }
}