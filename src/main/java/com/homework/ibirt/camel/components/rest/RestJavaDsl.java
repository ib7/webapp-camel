package com.homework.ibirt.camel.components.rest;

import com.homework.ibirt.dto.DepartmentDto;
import com.homework.ibirt.services.impl.DepartmentServiceImpl;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.support.DefaultMessage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class RestJavaDsl extends RouteBuilder {

    private final DepartmentServiceImpl departmentService;

    public RestJavaDsl(DepartmentServiceImpl departmentService) {
        this.departmentService = departmentService;
    }

    @Override
    public void configure() {

        from("rest:get:javadsl/departments?produces=application/json")
                .process(this::getAllDepartments);

        from("rest:get:javadsl/departments/{id}?produces=application/json")
                .outputType(DepartmentDto.class)
                .process(this::getDepartmentById);
    }

    private void getAllDepartments(final Exchange exchange) {
        List<DepartmentDto> departments = departmentService.getAllDepartments();

        final Message message = new DefaultMessage(exchange.getContext());
        message.setBody(departments);
        exchange.setMessage(message);
    }

    private void getDepartmentById(final Exchange exchange) {
        final UUID id = exchange.getMessage().getHeader("id", UUID.class);
        final DepartmentDto department = departmentService.getDepartment(id);

        final Message message = new DefaultMessage(exchange.getContext());
        message.setBody(department);
        exchange.setMessage(message);
    }
}