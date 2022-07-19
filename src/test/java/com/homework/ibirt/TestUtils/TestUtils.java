package com.homework.ibirt.TestUtils;

import com.homework.ibirt.jpa.models.Department;
import com.homework.ibirt.jpa.models.Employee;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.UUID;

import static com.homework.ibirt.stubs.DepartmentStub.DEPARTMENT_LOCATION_IASI;
import static com.homework.ibirt.stubs.DepartmentStub.createDefaultDepartment;
import static com.homework.ibirt.stubs.DepartmentStub.createDepartmentWithLocation;
import static com.homework.ibirt.stubs.EmployeeStub.createFirstEmployee;
import static com.homework.ibirt.stubs.EmployeeStub.createSecondEmployee;

@UtilityClass
public class TestUtils {

    public static final String API_URL = "/api/v1/";
    public static final String DEPARTMENTS_URL = API_URL + "departments";
    public static final String EMPLOYEES_URL = API_URL + "employees";

    public static final UUID RANDOM_UUID = UUID.randomUUID();
    public static final UUID UUID_FOR_DEPARTMENT = UUID.randomUUID();
    public static final Department DEPARTMENT_CHISINAU = createDefaultDepartment();
    public static final Department DEPARTMENT_IASI = createDepartmentWithLocation(DEPARTMENT_LOCATION_IASI);

    public static final List<Department> LIST_OF_TWO_DEPARTMENTS = List.of(DEPARTMENT_CHISINAU, DEPARTMENT_IASI);

    public static final Employee FIRST_EMPLOYEE = createFirstEmployee();
    public static final Employee SECOND_EMPLOYEE = createSecondEmployee();

    public static final List<Employee> LIST_OF_TWO_EMPLOYEES = List.of(FIRST_EMPLOYEE, SECOND_EMPLOYEE);
}