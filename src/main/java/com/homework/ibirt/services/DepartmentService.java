package com.homework.ibirt.services;

import com.homework.ibirt.dto.DepartmentDto;
import java.util.List;
import java.util.UUID;

public interface DepartmentService {
    List<DepartmentDto> getAllDepartments();

    DepartmentDto getDepartment(UUID id);

    DepartmentDto addDepartment(DepartmentDto departmentDTO);

    DepartmentDto updateDepartment(UUID id, DepartmentDto departmentDTO);
}