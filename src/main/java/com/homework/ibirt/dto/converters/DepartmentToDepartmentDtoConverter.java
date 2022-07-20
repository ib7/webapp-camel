package com.homework.ibirt.dto.converters;

import com.homework.ibirt.dto.DepartmentDto;
import com.homework.ibirt.jpa.models.Department;
import org.springframework.core.convert.converter.Converter;

public class DepartmentToDepartmentDtoConverter implements Converter<Department, DepartmentDto> {
    @Override
    public DepartmentDto convert(Department source) {
        return DepartmentDto
                .builder()
                .id(source.getId())
                .name(source.getName())
                .location(source.getLocation())
                .build();
    }
}
