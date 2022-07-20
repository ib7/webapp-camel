package com.homework.ibirt.services.impl;

import com.homework.ibirt.dto.DepartmentDto;
import com.homework.ibirt.exceptions.DepartmentNotFoundException;
import com.homework.ibirt.jpa.models.Department;
import com.homework.ibirt.jpa.repositories.DepartmentRepository;
import com.homework.ibirt.services.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final ConversionService conversionService;

    @Override
    public List<DepartmentDto> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll();
        List<DepartmentDto> departmentDtoList = new ArrayList<>();

        for (Department dep : departments) {
            departmentDtoList.add(conversionService.convert(dep, DepartmentDto.class));
        }

        return departmentDtoList;
    }

    @Override
    public DepartmentDto getDepartment(final UUID id) {
        return conversionService.convert(getDepartmentInternally(id), DepartmentDto.class);
    }

    @Override
    @Transactional
    public DepartmentDto addDepartment(final DepartmentDto departmentDto) {
        final Department department = conversionService.convert(departmentDto, Department.class);
        return conversionService.convert(departmentRepository.save(department), DepartmentDto.class);
    }

    @Override
    @Transactional
    public DepartmentDto updateDepartment(final UUID id, final DepartmentDto departmentDto) {
        final Department department = getDepartmentInternally(id);

        if (nonNull(departmentDto.getName())) {
            department.setName(departmentDto.getName());
        }
        if (nonNull(departmentDto.getLocation())) {
            department.setLocation(departmentDto.getLocation());
        }
        return conversionService.convert(department, DepartmentDto.class);
    }

    private Department getDepartmentInternally(final UUID id) {
        return departmentRepository.findById(id).orElseThrow(() ->
                new DepartmentNotFoundException("Department with id " + id + " was not found"));
    }
}