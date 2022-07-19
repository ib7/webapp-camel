package com.homework.ibirt.exceptions;


public class DepartmentNotFoundException extends RuntimeException {
    public DepartmentNotFoundException(final String message) {
        super(message);
    }
}