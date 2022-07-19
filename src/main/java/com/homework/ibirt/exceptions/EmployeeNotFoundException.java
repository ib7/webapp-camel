package com.homework.ibirt.exceptions;

import java.util.UUID;

import static java.lang.String.format;

public class EmployeeNotFoundException extends RuntimeException {

    public static final String PATTERN = "Employee with id %s was not found";

    public EmployeeNotFoundException(final String message) {
        super(message);
    }

    public EmployeeNotFoundException(final UUID uuid) {
        super(format(PATTERN, uuid));
    }
}
