package com.homework.ibirt.stubs;

import com.homework.ibirt.jpa.models.Department;
import com.homework.ibirt.jpa.models.Employee;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

import static com.homework.ibirt.TestUtils.TestUtils.DEPARTMENT_CHISINAU;
import static com.homework.ibirt.TestUtils.TestUtils.DEPARTMENT_IASI;
import static com.homework.ibirt.stubs.DepartmentStub.DEPARTMENT_LOCATION_IASI;
import static com.homework.ibirt.stubs.DepartmentStub.createDefaultDepartment;
import static com.homework.ibirt.stubs.DepartmentStub.createDepartmentWithLocation;

@UtilityClass
public final class EmployeeStub {

    public static final String EMP1_FIRST_NAME = "Igor";
    public static final String EMP1_LAST_NAME = "Birt";
    public static final Department EMP1_DEPARTMENT = DEPARTMENT_CHISINAU;
    public static final String EMP1_EMAIL = "abc@email.com";
    public static final String EMP1_PHONE_NUMBER = "60435782";
    public static final BigDecimal EMP1_SALARY = new BigDecimal("1200.5");

    public static final String EMP2_FIRST_NAME = "John";
    public static final String EMP2_LAST_NAME = "Lennon";
    public static final Department EMP2_DEPARTMENT = DEPARTMENT_IASI;
    public static final String EMP2_EMAIL = "zxy@email.com";
    public static final String EMP2_PHONE_NUMBER = "79144982";
    public static final BigDecimal EMP2_SALARY = new BigDecimal("1100.5");

    public static Employee createFirstEmployee() {
        return Employee.builder()
                .firstName(EMP1_FIRST_NAME)
                .lastName(EMP1_LAST_NAME)
                .department(createDefaultDepartment())
                .email(EMP1_EMAIL)
                .phoneNumber(EMP1_PHONE_NUMBER)
                .salary(EMP1_SALARY)
                .build();
    }

    public static Employee createSecondEmployee() {
        return Employee.builder()
                .firstName(EMP2_FIRST_NAME)
                .lastName(EMP2_LAST_NAME)
                .department(createDepartmentWithLocation(DEPARTMENT_LOCATION_IASI))
                .email(EMP2_EMAIL)
                .phoneNumber(EMP2_PHONE_NUMBER)
                .salary(EMP2_SALARY)
                .build();
    }
}