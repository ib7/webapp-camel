package com.homework.ibirt.services.impl;

import com.homework.ibirt.dto.DepartmentDto;
import com.homework.ibirt.exceptions.DepartmentNotFoundException;
import com.homework.ibirt.jpa.models.Department;
import com.homework.ibirt.jpa.repositories.DepartmentRepository;
import com.homework.ibirt.services.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.homework.ibirt.dto.DepartmentDto.*;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    public List<DepartmentDto> getAllDepartments() {
        return departmentRepository.findAll()
                .stream()
                .map(DepartmentDto::fromDepartment)
                .collect(toList());
    }

    @Override
    public DepartmentDto getDepartment(final UUID id) {
        return fromDepartment(getDepartmentInternally(id));
        }

    @Override
    @Transactional
    public DepartmentDto addDepartment(final DepartmentDto departmentDto) {
        final Department department = toDepartment(departmentDto);
        return fromDepartment(departmentRepository.save(department));

    }

    @Override
    @Transactional
    public DepartmentDto updateDepartment(final UUID id, final DepartmentDto departmentDto) {
        final Department department = getDepartmentInternally(id);

        if (nonNull(departmentDto.getName())){
            department.setName(departmentDto.getName());
        }
        if (nonNull(departmentDto.getLocation())){
            department.setLocation(departmentDto.getLocation());
        }
        return fromDepartment(department);
    }

    private Department getDepartmentInternally(final UUID id) {
        return departmentRepository.findById(id).orElseThrow(() ->
                new DepartmentNotFoundException("Department with id " + id + " was not found"));
    }
}