package com.homework.ibirt.dto;

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
}