package com.homework.ibirt.config;

import com.homework.ibirt.dto.converters.DepartmentDtoToDepartmentConverter;
import com.homework.ibirt.dto.converters.DepartmentToDepartmentDtoConverter;
import com.homework.ibirt.dto.converters.EmployeeDtoToEmployeeConverter;
import com.homework.ibirt.dto.converters.EmployeeToEmployeeDtoConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

@Configuration
public class ConversionServiceConfig {

    @Bean
    public ConversionService conversionService () {
        DefaultConversionService service = new DefaultConversionService();

        service.addConverter(new DepartmentToDepartmentDtoConverter());
        service.addConverter(new DepartmentDtoToDepartmentConverter());
        service.addConverter(new EmployeeToEmployeeDtoConverter(service));
        service.addConverter(new EmployeeDtoToEmployeeConverter(service));

        return service;
    }
}