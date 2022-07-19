package com.homework.ibirt.exceptions;

import com.homework.ibirt.exceptions.dto.ErrorResponseDto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String DEPARTMENT_NOT_FOUND = "Department not found";
    private static final String EMPLOYEE_NOT_FOUND = "Employee not found";

    private static final String BAD_PARAMETER_VALUE = "Bad parameter value";
    private static final String EMAIL_OR_PHONE_NUMBER_INVALID = "Bad parameter for email and/or phone number";

    @ExceptionHandler(DepartmentNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> onDepartmentNotFound(final HttpServletRequest request,
                                                                 final DepartmentNotFoundException e) {

        final ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .error(DEPARTMENT_NOT_FOUND)
                .message(e.getMessage())
                .path(request.getServletPath())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> onEmployeeNotFound(final HttpServletRequest request,
                                                               final EmployeeNotFoundException e) {

        final ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .error(EMPLOYEE_NOT_FOUND)
                .message(e.getMessage())
                .path(request.getServletPath())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDto> onEmployeeNotFound(final HttpServletRequest request,
                                                               final MethodArgumentTypeMismatchException e) {

        String message = "Parameter: " + e.getName() + " has invalid value: " + e.getValue();

        final ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(BAD_PARAMETER_VALUE)
                .message(message)
                .path(request.getServletPath())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleUniqueConstraint(final HttpServletRequest request,
                                                    final DataIntegrityViolationException ignoredE) {

        String message = "Please check your email and phone number, one of them is already taken";

        final ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(EMAIL_OR_PHONE_NUMBER_INVALID)
                .message(message)
                .path(request.getServletPath())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
    }
}