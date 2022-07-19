package com.homework.ibirt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homework.ibirt.dto.DepartmentDto;
import com.homework.ibirt.jpa.models.Department;
import com.homework.ibirt.jpa.repositories.DepartmentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.homework.ibirt.TestUtils.TestUtils.DEPARTMENTS_URL;
import static com.homework.ibirt.TestUtils.TestUtils.DEPARTMENT_CHISINAU;
import static com.homework.ibirt.TestUtils.TestUtils.DEPARTMENT_IASI;
import static com.homework.ibirt.TestUtils.TestUtils.RANDOM_UUID;
import static com.homework.ibirt.stubs.DepartmentStub.DEPARTMENT_LOCATION_CHISINAU;
import static com.homework.ibirt.stubs.DepartmentStub.DEPARTMENT_LOCATION_IASI;
import static com.homework.ibirt.stubs.DepartmentStub.DEPARTMENT_NAME_IT;
import static com.homework.ibirt.stubs.DepartmentStub.createDefaultDepartment;
import static com.homework.ibirt.stubs.DepartmentStub.createDepartmentWithLocation;
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
class DepartmentControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    void tearDown() {
        departmentRepository.deleteAll();
    }

    @Test
    void getAllDepartments() throws Exception {
        departmentRepository.saveAll(List.of(DEPARTMENT_CHISINAU, DEPARTMENT_IASI));

        mvc.perform(get(DEPARTMENTS_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name", is(DEPARTMENT_NAME_IT)))
                .andExpect(jsonPath("$[0].location", is(DEPARTMENT_LOCATION_CHISINAU)))
                .andExpect(jsonPath("$[1].name", is(DEPARTMENT_NAME_IT)))
                .andExpect(jsonPath("$[1].location", is(DEPARTMENT_LOCATION_IASI)));
    }

    @Test
    void getDepartment() throws Exception {
        final Department savedDepartment = departmentRepository.save(createDefaultDepartment());

        mvc.perform(get(DEPARTMENTS_URL + "/" + savedDepartment.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("name", is(DEPARTMENT_NAME_IT)))
                .andExpect(jsonPath("location", is(DEPARTMENT_LOCATION_CHISINAU)));
    }

    @Test
    void getDepartmentWhenNoSuchIdShouldThrownAnException() throws Exception {
        mvc.perform(get(DEPARTMENTS_URL + "/" + RANDOM_UUID))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("error", is("Department not found")))
                .andExpect(jsonPath("message", is(format("Department with id %s was not found", RANDOM_UUID)))
                );
    }

    @Test
    void addDepartment() throws Exception {
        final DepartmentDto departmentDtoActual = DepartmentDto.fromDepartment(createDefaultDepartment());

        mvc.perform(post(DEPARTMENTS_URL)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(departmentDtoActual))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON));

        assertThat(departmentRepository.count()).isEqualTo(1L);

        final Department departmentInDB = departmentRepository.findAll().get(0);
        assertThat(departmentInDB.getName()).isEqualTo(departmentDtoActual.getName());
        assertThat(departmentInDB.getLocation()).isEqualTo(departmentDtoActual.getLocation());
    }

    @Test
    void updateDepartment() throws Exception {
        final Department savedDepartment = departmentRepository.save(createDefaultDepartment());
        final Department departmentIasiForUpdatingInDB = createDepartmentWithLocation(DEPARTMENT_LOCATION_IASI);

        mvc.perform(put(DEPARTMENTS_URL + "/" + savedDepartment.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(departmentIasiForUpdatingInDB))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("name", is(DEPARTMENT_NAME_IT)))
                .andExpect(jsonPath("location", is(DEPARTMENT_LOCATION_IASI)));

        final Department departmentInDB = departmentRepository.findAll().get(0);
        assertThat(departmentInDB.getName()).isEqualTo(departmentIasiForUpdatingInDB.getName());
        assertThat(departmentInDB.getLocation()).isEqualTo(departmentIasiForUpdatingInDB.getLocation());
    }

    @Test
    void updateDepartmentWhenNoSuchIdShouldThrownAnException() throws Exception {
        final Department departmentIasiForUpdatingInDB = createDepartmentWithLocation(DEPARTMENT_LOCATION_IASI);

        mvc.perform(put(DEPARTMENTS_URL + "/" + RANDOM_UUID)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(departmentIasiForUpdatingInDB))
                )
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("error", is("Department not found")))
                .andExpect(jsonPath("message", is(format("Department with id %s was not found", RANDOM_UUID)))
                );
    }
}