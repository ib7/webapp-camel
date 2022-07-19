package com.homework.ibirt.camel.components.rest;

import com.homework.ibirt.dto.DepartmentDto;
import com.homework.ibirt.dto.EmployeeDto;
import com.homework.ibirt.services.impl.DepartmentServiceImpl;
import com.homework.ibirt.services.impl.EmployeeServiceImpl;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.support.DefaultMessage;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class RestDsl extends RouteBuilder {

    private final DepartmentServiceImpl departmentService;
    private final EmployeeServiceImpl employeeService;

    public RestDsl(DepartmentServiceImpl departmentService, EmployeeServiceImpl employeeService) {
        this.departmentService = departmentService;
        this.employeeService = employeeService;
    }

    @Override
    public void configure() {

        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.auto);

        rest()
                .consumes(MediaType.APPLICATION_JSON_VALUE).produces(MediaType.APPLICATION_JSON_VALUE)
                .get("departments").outType(DepartmentDto.class).to("direct:get-all-departments")
                .get("departments/{id}").outType(DepartmentDto.class).to("direct:get-departments-by-id")
                .post("departments").type(DepartmentDto.class).outType(DepartmentDto.class).to("direct:add-department")
                .put("departments/{id}").type(DepartmentDto.class).outType(DepartmentDto.class).to("direct:edit-department")

                .get("employees").outType(EmployeeDto.class).to("direct:get-all-employees")
                .get("employees/{id}").outType(EmployeeDto.class).to("direct:get-employee-by-id")
                .post("employees").type(EmployeeDto.class).outType(EmployeeDto.class).to("direct:add-employee")
                .put("employees/{id}").type(EmployeeDto.class).outType(EmployeeDto.class).to("direct:edit-employee");

        from("direct:get-all-departments").process(this::getAllDepartments);
        from("direct:get-departments-by-id").process(this::getDepartmentById);
        from("direct:add-department").process(this::addDepartment);
        from("direct:edit-department").process(this::editDepartment);

        from("direct:get-all-employees").process(this::getAllEmployees);
        from("direct:get-employee-by-id").process(this::getEmployeeById);
        from("direct:add-employee").process(this::addEmployee);
        from("direct:edit-employee").process(this::editEmployee);
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

    private void addDepartment(final Exchange exchange) {
        final DepartmentDto department = exchange.getMessage().getBody(DepartmentDto.class);

        final Message message = new DefaultMessage(exchange.getContext());
        message.setBody(departmentService.addDepartment(department));
        exchange.setMessage(message);

    }

    private void editDepartment(final Exchange exchange) {
        final UUID id = exchange.getMessage().getHeader("id", UUID.class);
        final DepartmentDto department = exchange.getMessage().getBody(DepartmentDto.class);

        final Message message = new DefaultMessage(exchange.getContext());
        message.setBody(departmentService.updateDepartment(id, department));
        exchange.setMessage(message);
    }

    private void getAllEmployees(final Exchange exchange) {
        List<EmployeeDto> employees = employeeService.getAllEmployees();
        final Message message = new DefaultMessage(exchange.getContext());
        message.setBody(employees);
        exchange.setMessage(message);
    }

    private void getEmployeeById(final Exchange exchange) {
        final UUID id = exchange.getMessage().getHeader("id", UUID.class);
        final EmployeeDto employee = employeeService.getEmployee(id);

        final Message message = new DefaultMessage(exchange.getContext());
        message.setBody(employee);
        exchange.setMessage(message);
    }

    private void addEmployee(final Exchange exchange) {
        final EmployeeDto employee = exchange.getMessage().getBody(EmployeeDto.class);

        final Message message = new DefaultMessage(exchange.getContext());
        message.setBody(employeeService.addEmployee(employee));
        exchange.setMessage(message);
    }

    private void editEmployee(final Exchange exchange) {
        final UUID id = exchange.getMessage().getHeader("id",UUID.class);
        final EmployeeDto employee = exchange.getMessage().getBody(EmployeeDto.class);

        final Message message = new DefaultMessage(exchange.getContext());
        message.setBody(employeeService.updateEmployee(id, employee));
        exchange.setMessage(message);
    }
}