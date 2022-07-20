package com.homework.ibirt.dto;

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
}