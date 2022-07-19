package com.homework.ibirt.dto;

import com.homework.ibirt.jpa.models.Department;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class DepartmentDto {
    private UUID id;

    @NotBlank(message = "Department name should not be empty/blank/null")
    private String name;

    @NotBlank(message = "Department location should not be empty/blank/null")
    private String location;

    public static DepartmentDto fromDepartment(final Department department) {
        return DepartmentDto
                .builder()
                .id(department.getId())
                .name(department.getName())
                .location(department.getLocation())
                .build();
    }

    public static Department toDepartment(final DepartmentDto departmentDto) {
        return Department.builder()
                .name(departmentDto.getName())
                .location(departmentDto.getLocation())
                .build();
    }
}