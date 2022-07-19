package com.homework.ibirt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homework.ibirt.dto.EmployeeDto;
import com.homework.ibirt.jpa.models.Department;
import com.homework.ibirt.jpa.models.Employee;
import com.homework.ibirt.jpa.repositories.DepartmentRepository;
import com.homework.ibirt.jpa.repositories.EmployeeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static com.homework.ibirt.TestUtils.TestUtils.EMPLOYEES_URL;
import static com.homework.ibirt.TestUtils.TestUtils.RANDOM_UUID;
import static com.homework.ibirt.dto.DepartmentDto.fromDepartment;
import static com.homework.ibirt.dto.EmployeeDto.fromEmployee;
import static com.homework.ibirt.stubs.DepartmentStub.DEPARTMENT_LOCATION_CHISINAU;
import static com.homework.ibirt.stubs.DepartmentStub.DEPARTMENT_LOCATION_IASI;
import static com.homework.ibirt.stubs.DepartmentStub.DEPARTMENT_NAME_IT;
import static com.homework.ibirt.stubs.DepartmentStub.createDefaultDepartment;
import static com.homework.ibirt.stubs.DepartmentStub.createDepartmentWithLocation;
import static com.homework.ibirt.stubs.EmployeeStub.EMP1_EMAIL;
import static com.homework.ibirt.stubs.EmployeeStub.EMP1_FIRST_NAME;
import static com.homework.ibirt.stubs.EmployeeStub.EMP1_LAST_NAME;
import static com.homework.ibirt.stubs.EmployeeStub.EMP1_PHONE_NUMBER;
import static com.homework.ibirt.stubs.EmployeeStub.EMP1_SALARY;
import static com.homework.ibirt.stubs.EmployeeStub.EMP2_EMAIL;
import static com.homework.ibirt.stubs.EmployeeStub.EMP2_FIRST_NAME;
import static com.homework.ibirt.stubs.EmployeeStub.EMP2_LAST_NAME;
import static com.homework.ibirt.stubs.EmployeeStub.EMP2_PHONE_NUMBER;
import static com.homework.ibirt.stubs.EmployeeStub.EMP2_SALARY;
import static com.homework.ibirt.stubs.EmployeeStub.createFirstEmployee;
import static com.homework.ibirt.stubs.EmployeeStub.createSecondEmployee;
import static java.lang.String.format;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    void tearDown() {
        employeeRepository.deleteAll();
        departmentRepository.deleteAll();
    }

    @Test
    void getAllEmployees() throws Exception {
        employeeRepository.saveAll(List.of(createFirstEmployee(), createSecondEmployee()));

        mvc.perform(get(EMPLOYEES_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$[0].firstName", is(EMP1_FIRST_NAME)))
                .andExpect(jsonPath("$[0].lastName", is(EMP1_LAST_NAME)))
                .andExpect(jsonPath("$[0].email", is(EMP1_EMAIL)))
                .andExpect(jsonPath("$[0].phoneNumber", is(EMP1_PHONE_NUMBER)))
                .andExpect(jsonPath("$[0].departmentDto.name", is(DEPARTMENT_NAME_IT)))
                .andExpect(jsonPath("$[0].departmentDto.location", is(DEPARTMENT_LOCATION_CHISINAU)))
                .andExpect(jsonPath("$[0].salary", is(EMP1_SALARY.doubleValue())))
                .andExpect(jsonPath("$[1].firstName", is(EMP2_FIRST_NAME)))
                .andExpect(jsonPath("$[1].lastName", is(EMP2_LAST_NAME)))
                .andExpect(jsonPath("$[1].email", is(EMP2_EMAIL)))
                .andExpect(jsonPath("$[1].phoneNumber", is(EMP2_PHONE_NUMBER)))
                .andExpect(jsonPath("$[1].departmentDto.name", is(DEPARTMENT_NAME_IT)))
                .andExpect(jsonPath("$[1].departmentDto.location", is(DEPARTMENT_LOCATION_IASI)))
                .andExpect(jsonPath("$[1].salary", is(EMP2_SALARY.doubleValue())));
    }

    @Test
    void getEmployee() throws Exception {
        final Employee savedEmployee = employeeRepository.save(createFirstEmployee());

        mvc.perform(get(EMPLOYEES_URL + "/" + savedEmployee.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("firstName", is(EMP1_FIRST_NAME)))
                .andExpect(jsonPath("lastName", is(EMP1_LAST_NAME)))
                .andExpect(jsonPath("email", is(EMP1_EMAIL)))
                .andExpect(jsonPath("phoneNumber", is(EMP1_PHONE_NUMBER)))
                .andExpect(jsonPath("departmentDto.name", is(DEPARTMENT_NAME_IT)))
                .andExpect(jsonPath("departmentDto.location", is(DEPARTMENT_LOCATION_CHISINAU)))
                .andExpect(jsonPath("salary", is(EMP1_SALARY.doubleValue())));
    }

    @Test
    void getEmployeeWhenNoSuchIdShouldThrownAnException() throws Exception {
        mvc.perform(get(EMPLOYEES_URL + "/" + RANDOM_UUID))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("error", is("Employee not found")))
                .andExpect(jsonPath("message", is(format("Employee with id %s was not found", RANDOM_UUID)))
                );
    }

    @Test
    void addEmployee() throws Exception {
        final Department departmentChisinauForSavingInDB = createDefaultDepartment();

        departmentRepository.save(departmentChisinauForSavingInDB);

        assertThat(employeeRepository.count()).isEqualTo(0L);

        assertThat(departmentChisinauForSavingInDB.getId()).isNotNull();

        final EmployeeDto employeeDtoActual = EmployeeDto.builder()
                .email("email@email.com")
                .firstName("Igor")
                .lastName("Birt")
                .salary(new BigDecimal(10000))
                .phoneNumber("37369311629")
                .departmentDto(fromDepartment(departmentChisinauForSavingInDB)).build();


        mvc.perform(post(EMPLOYEES_URL)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(employeeDtoActual))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON));

        assertThat(employeeRepository.count()).isEqualTo(1L);
        final Department departmentInDB = employeeRepository.findAll().get(0).getDepartment();
        final Employee employeeInDB = employeeRepository.findAll().get(0);
        assertThat(departmentInDB).isEqualTo(departmentChisinauForSavingInDB);
        assertThat(employeeInDB.getEmail()).isEqualTo(employeeDtoActual.getEmail());
        assertThat(employeeInDB.getPhoneNumber()).isEqualTo(employeeDtoActual.getPhoneNumber());
    }

    @Test
    void addEmployeeWhenThereIsNoSuchDepartmentInDBShouldThrownAnException() throws Exception {
        final Department departmentChisinauNotForSavingInDB = createDefaultDepartment();
        departmentChisinauNotForSavingInDB.setId(RANDOM_UUID);

        final EmployeeDto employeeDtoActual = EmployeeDto.builder()
                .email("email@email.com")
                .firstName("Igor")
                .lastName("Birt")
                .salary(new BigDecimal(10000))
                .phoneNumber("37369311629")
                .departmentDto(fromDepartment(departmentChisinauNotForSavingInDB)).build();

        mvc.perform(post(EMPLOYEES_URL)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(employeeDtoActual))
                )
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("error", is("Department not found")))
                .andExpect(jsonPath("message", is(format("Department with id %s was not found", RANDOM_UUID)))
                );

        assertThat(departmentRepository.count()).isEqualTo(0L);
        assertThat(employeeRepository.count()).isEqualTo(0L);
    }

    @Test
    void updateEmployee() throws Exception {
        final Employee savedEmployee = employeeRepository.save(createFirstEmployee());

        final Department departmentIasiForSavingInDB = createDepartmentWithLocation(DEPARTMENT_LOCATION_IASI);
        departmentRepository.save(departmentIasiForSavingInDB);

        final Employee employeeForUpdatingInDB = createSecondEmployee();
        employeeForUpdatingInDB.setDepartment(departmentIasiForSavingInDB);

        final EmployeeDto employeeDtoForUpdatingInDB = fromEmployee(employeeForUpdatingInDB);

        mvc.perform(put(EMPLOYEES_URL + "/" + savedEmployee.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(employeeDtoForUpdatingInDB))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("firstName", is(EMP2_FIRST_NAME)))
                .andExpect(jsonPath("lastName", is(EMP2_LAST_NAME)))
                .andExpect(jsonPath("email", is(EMP2_EMAIL)))
                .andExpect(jsonPath("phoneNumber", is(EMP2_PHONE_NUMBER)))
                .andExpect(jsonPath("departmentDto.name", is(DEPARTMENT_NAME_IT)))
                .andExpect(jsonPath("departmentDto.location", is(DEPARTMENT_LOCATION_IASI)))
                .andExpect(jsonPath("salary", is(EMP2_SALARY.doubleValue())));
    }
}