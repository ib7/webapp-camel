package com.homework.ibirt.services.impl;

import com.homework.ibirt.dto.DepartmentDto;
import com.homework.ibirt.exceptions.DepartmentNotFoundException;
import com.homework.ibirt.jpa.repositories.DepartmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.homework.ibirt.TestUtils.TestUtils.DEPARTMENT_CHISINAU;
import static com.homework.ibirt.TestUtils.TestUtils.DEPARTMENT_IASI;
import static com.homework.ibirt.TestUtils.TestUtils.LIST_OF_TWO_DEPARTMENTS;
import static com.homework.ibirt.TestUtils.TestUtils.RANDOM_UUID;
import static com.homework.ibirt.dto.DepartmentDto.fromDepartment;
import static com.homework.ibirt.stubs.DepartmentStub.DEPARTMENT_LOCATION_CHISINAU;
import static com.homework.ibirt.stubs.DepartmentStub.DEPARTMENT_NAME_IT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceImplTest {

    @Mock
    DepartmentRepository departmentRepository;

    @InjectMocks
    DepartmentServiceImpl departmentService;

    @Test
    void getAllDepartmentsShouldReturnListOfDepartmentsDto() {
        when(departmentRepository.findAll()).thenReturn(LIST_OF_TWO_DEPARTMENTS);

        final List<DepartmentDto> allDepartmentsFromDB = departmentService.getAllDepartments();

        assertThat(allDepartmentsFromDB.size()).isEqualTo(LIST_OF_TWO_DEPARTMENTS.size());

        allDepartmentsFromDB.forEach(departmentDto -> {
            assertThat(departmentDto.getName()).isEqualTo(DEPARTMENT_NAME_IT);
            assertThat(allDepartmentsFromDB.get(0).getLocation()).isEqualTo(LIST_OF_TWO_DEPARTMENTS.get(0).getLocation());
            assertThat(allDepartmentsFromDB.get(1).getLocation()).isEqualTo(LIST_OF_TWO_DEPARTMENTS.get(1).getLocation());
        });
    }

    @Test
    void getDepartmentShouldReturnDepartmentDto() {
        when(departmentRepository.findById(RANDOM_UUID)).thenReturn(Optional.of(DEPARTMENT_CHISINAU));

        final DepartmentDto departmentFromDB = departmentService.getDepartment(RANDOM_UUID);

        assertAll(
                () -> assertThat(departmentFromDB.getName()).isEqualTo(DEPARTMENT_NAME_IT),
                () -> assertThat(departmentFromDB.getLocation()).isEqualTo(DEPARTMENT_LOCATION_CHISINAU)
        );
    }

    @Test
    void getDepartmentWhenNoSuchIdShouldThrowAnException() {
        when(departmentRepository.findById(RANDOM_UUID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> departmentService.getDepartment(RANDOM_UUID))
                .isExactlyInstanceOf(DepartmentNotFoundException.class)
                .hasMessage("Department with id " + RANDOM_UUID + " was not found");

    }

    @Test
    void addDepartmentShouldReturnDepartmentDto() {
        final DepartmentDto departmentDto = fromDepartment(DEPARTMENT_CHISINAU);
        final DepartmentDto departmentSaved = departmentService.addDepartment(departmentDto);

        verify(departmentRepository).save(DEPARTMENT_CHISINAU);

        assertThat(departmentSaved).isEqualTo(departmentDto);
    }

    @Test
    void updateDepartmentWhenThereAreChangesInFieldsShouldReturnChangedDepartmentDto() {
        final DepartmentDto departmentDtoIasiForUpdate = fromDepartment(DEPARTMENT_IASI);

        when(departmentRepository.findById(RANDOM_UUID)).thenReturn(Optional.of(DEPARTMENT_CHISINAU));

        final DepartmentDto departmentUpdatedInDB = departmentService.updateDepartment(RANDOM_UUID, departmentDtoIasiForUpdate);

        assertThat(departmentUpdatedInDB.getLocation()).isEqualTo(DEPARTMENT_IASI.getLocation());
    }

    @Test
    void updateDepartmentWhenThereAreNoChangesInFieldsShouldReturnTheSameDepartmentDto() {
        final DepartmentDto departmentDtoWithAllNullFields = DepartmentDto.builder().build();

        when(departmentRepository.findById(RANDOM_UUID)).thenReturn(Optional.of(DEPARTMENT_CHISINAU));

        final DepartmentDto departmentUpdatedInDB = departmentService.updateDepartment(RANDOM_UUID, departmentDtoWithAllNullFields);

        assertAll(
                () -> assertThat(departmentUpdatedInDB.getLocation()).isEqualTo(DEPARTMENT_CHISINAU.getLocation()),
                () -> assertThat(departmentUpdatedInDB.getName()).isEqualTo(DEPARTMENT_CHISINAU.getName())
        );
    }

    @Test
    void updateDepartmentWhenNoSuchDepartmentIdShouldThrownAnException() {
        final DepartmentDto departmentDto = fromDepartment(DEPARTMENT_CHISINAU);

        when(departmentRepository.findById(RANDOM_UUID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> departmentService.updateDepartment(RANDOM_UUID, departmentDto))
                .isExactlyInstanceOf(DepartmentNotFoundException.class)
                .hasMessage("Department with id " + RANDOM_UUID + " was not found");
    }
}