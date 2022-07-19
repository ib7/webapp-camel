package com.homework.ibirt.exceptions.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class ErrorResponseDto {
    LocalDateTime timestamp = LocalDateTime.now();
    int status;
    String error;
    String message;
    String path;

    public ErrorResponseDto(final int status, final String error, final String message, final String path) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }
}