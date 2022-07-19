package com.homework.ibirt.services.impl;

import com.homework.ibirt.dto.EmployeeDto;
import com.homework.ibirt.exceptions.DepartmentNotFoundException;
import com.homework.ibirt.exceptions.EmployeeNotFoundException;
import com.homework.ibirt.jpa.models.Employee;
import com.homework.ibirt.jpa.repositories.DepartmentRepository;
import com.homework.ibirt.jpa.repositories.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.homework.ibirt.TestUtils.TestUtils.DEPARTMENT_CHISINAU;
import static com.homework.ibirt.TestUtils.TestUtils.DEPARTMENT_IASI;
import static com.homework.ibirt.TestUtils.TestUtils.FIRST_EMPLOYEE;
import static com.homework.ibirt.TestUtils.TestUtils.LIST_OF_TWO_EMPLOYEES;
import static com.homework.ibirt.TestUtils.TestUtils.RANDOM_UUID;
import static com.homework.ibirt.TestUtils.TestUtils.SECOND_EMPLOYEE;
import static com.homework.ibirt.TestUtils.TestUtils.UUID_FOR_DEPARTMENT;
import static com.homework.ibirt.dto.DepartmentDto.fromDepartment;
import static com.homework.ibirt.dto.EmployeeDto.fromEmployee;
import static com.homework.ibirt.stubs.EmployeeStub.EMP1_DEPARTMENT;
import static com.homework.ibirt.stubs.EmployeeStub.EMP1_EMAIL;
import static com.homework.ibirt.stubs.EmployeeStub.EMP1_FIRST_NAME;
import static com.homework.ibirt.stubs.EmployeeStub.EMP1_LAST_NAME;
import static com.homework.ibirt.stubs.EmployeeStub.EMP1_PHONE_NUMBER;
import static com.homework.ibirt.stubs.EmployeeStub.EMP1_SALARY;
import static com.homework.ibirt.stubs.EmployeeStub.EMP2_DEPARTMENT;
import static com.homework.ibirt.stubs.EmployeeStub.EMP2_EMAIL;
import static com.homework.ibirt.stubs.EmployeeStub.EMP2_FIRST_NAME;
import static com.homework.ibirt.stubs.EmployeeStub.EMP2_LAST_NAME;
import static com.homework.ibirt.stubs.EmployeeStub.EMP2_PHONE_NUMBER;
import static com.homework.ibirt.stubs.EmployeeStub.EMP2_SALARY;
import static com.homework.ibirt.stubs.EmployeeStub.createFirstEmployee;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    void getAllEmployeesShouldReturnListOfEmployeesDto() {
        when(employeeRepository.findAll()).thenReturn(LIST_OF_TWO_EMPLOYEES);

        final List<EmployeeDto> allEmployeesFromDB = employeeService.getAllEmployees();

        assertThat(allEmployeesFromDB.size()).isEqualTo(LIST_OF_TWO_EMPLOYEES.size());

        allEmployeesFromDB.forEach(employeeDto -> {
            assertThat(employeeDto.getFirstName()).isIn(EMP1_FIRST_NAME, EMP2_FIRST_NAME);
            assertThat(employeeDto.getLastName()).isIn(EMP1_LAST_NAME, EMP2_LAST_NAME);
            assertThat(employeeDto.getDepartmentDto()).isIn(fromDepartment(EMP1_DEPARTMENT), fromDepartment(EMP2_DEPARTMENT));
            assertThat(employeeDto.getEmail()).isIn(EMP1_EMAIL, EMP2_EMAIL);
            assertThat(employeeDto.getPhoneNumber()).isIn(EMP1_PHONE_NUMBER, EMP2_PHONE_NUMBER);
            assertThat(employeeDto.getSalary()).isIn(EMP1_SALARY, EMP2_SALARY);
        });
    }

    @Test
    void getEmployeesShouldReturnEmployeeDto() {
        when(employeeRepository.findById(RANDOM_UUID)).thenReturn(Optional.of(FIRST_EMPLOYEE));

        final EmployeeDto employeeFromDB = employeeService.getEmployee(RANDOM_UUID);

        assertAll(
                () -> assertThat(employeeFromDB.getFirstName()).isEqualTo(EMP1_FIRST_NAME),
                () -> assertThat(employeeFromDB.getLastName()).isEqualTo(EMP1_LAST_NAME),
                () -> assertThat(employeeFromDB.getDepartmentDto()).isEqualTo(fromDepartment(EMP1_DEPARTMENT)),
                () -> assertThat(employeeFromDB.getEmail()).isEqualTo(EMP1_EMAIL),
                () -> assertThat(employeeFromDB.getPhoneNumber()).isEqualTo(EMP1_PHONE_NUMBER),
                () -> assertThat(employeeFromDB.getSalary()).isEqualTo(EMP1_SALARY)
        );
    }

    @Test
    void getEmployeesWhenNoSuchIdShouldThrowAnException() {
        when(employeeRepository.findById(RANDOM_UUID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.getEmployee(RANDOM_UUID))
                .isExactlyInstanceOf(EmployeeNotFoundException.class)
                .hasMessage("Employee with id " + RANDOM_UUID + " was not found");
    }

    @Test
    void addEmployeeShouldReturnEmployeeDto() {
        when(departmentRepository.findById(FIRST_EMPLOYEE.getDepartment().getId())).thenReturn(Optional.of(DEPARTMENT_CHISINAU));

        final EmployeeDto employeeDto = fromEmployee(FIRST_EMPLOYEE);
        final EmployeeDto savedEmployee = employeeService.addEmployee(employeeDto);

        verify(employeeRepository).save(FIRST_EMPLOYEE);

        assertThat(savedEmployee).isEqualTo(employeeDto);
    }

    @Test
    void addEmployeeWhenNoSuchDepartmentShouldThrownAnException() {
        when(departmentRepository.findById(UUID_FOR_DEPARTMENT)).thenReturn(Optional.empty());

        final EmployeeDto employeeDto = fromEmployee(FIRST_EMPLOYEE);
        employeeDto.getDepartmentDto().setId(UUID_FOR_DEPARTMENT);

        assertThatThrownBy(() -> employeeService.addEmployee(employeeDto))
                .isExactlyInstanceOf(DepartmentNotFoundException.class)
                .hasMessage("Department with id " + UUID_FOR_DEPARTMENT + " was not found");

        verify(employeeRepository, never()).save(any());
    }

    @Test
    void updateEmployeeWhenThereAreChangesInFieldsShouldReturnChangedEmployeeDto() {
        final EmployeeDto EmployeeDtoForUpdate = EmployeeDto.fromEmployee(SECOND_EMPLOYEE);

        final Employee firstEmployee = createFirstEmployee();

        when(employeeRepository.findById(RANDOM_UUID)).thenReturn(Optional.of(firstEmployee));
        when(departmentRepository.findById(EmployeeDtoForUpdate.getDepartmentDto().getId())).thenReturn(Optional.of(DEPARTMENT_IASI));

        final EmployeeDto employeeUpdatedInDB = employeeService.updateEmployee(RANDOM_UUID, EmployeeDtoForUpdate);

        assertThat(employeeUpdatedInDB).isEqualTo(EmployeeDtoForUpdate);
    }

    @Test
    void updateEmployeeWhenThereAreNoChangesInFieldsShouldReturnTheSameEmployeeDto() {
        final EmployeeDto employeeDtoWithAllNullFields = EmployeeDto.builder().build();

        when(employeeRepository.findById(RANDOM_UUID)).thenReturn(Optional.of(FIRST_EMPLOYEE));

        final EmployeeDto employeeUpdatedInDB = employeeService.updateEmployee(RANDOM_UUID, employeeDtoWithAllNullFields);

        assertThat(employeeUpdatedInDB).isEqualTo(fromEmployee(FIRST_EMPLOYEE));
    }

    @Test
    void updateEmployeeWhenNoSuchEmployeeIdShouldThrownAnException() {
        final EmployeeDto employeeDto = fromEmployee(FIRST_EMPLOYEE);

        when(employeeRepository.findById(RANDOM_UUID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.updateEmployee(RANDOM_UUID, employeeDto))
                .isExactlyInstanceOf(EmployeeNotFoundException.class)
                .hasMessage("Employee with id " + RANDOM_UUID + " was not found");
    }
}