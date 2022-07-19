package com.homework.ibirt.stubs;

import com.homework.ibirt.jpa.models.Department;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class DepartmentStub {

    public static final String DEPARTMENT_NAME_IT = "IT";
    public static final String DEPARTMENT_LOCATION_CHISINAU = "Chisinau";
    public static final String DEPARTMENT_LOCATION_IASI = "Iasi";

    public static Department createDefaultDepartment() {
        return createDepartmentWithLocation(DEPARTMENT_LOCATION_CHISINAU);
    }

    public static Department createDepartmentWithLocation(String location) {
        return new Department(DEPARTMENT_NAME_IT, location);
    }
}