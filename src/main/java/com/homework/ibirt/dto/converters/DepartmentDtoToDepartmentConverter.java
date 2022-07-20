package com.homework.ibirt.dto.converters;

import com.homework.ibirt.dto.DepartmentDto;
import com.homework.ibirt.jpa.models.Department;
import org.springframework.core.convert.converter.Converter;

public class DepartmentDtoToDepartmentConverter implements Converter<DepartmentDto, Department> {
    @Override
    public Department convert(DepartmentDto source) {
        return Department
                .builder()
                .name(source.getName())
                .location(source.getLocation())
                .build();
    }
}