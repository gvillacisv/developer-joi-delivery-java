package com.tw.joi.delivery.controller;

import com.tw.joi.delivery.dto.response.ValidationErrorResponse;
import com.tw.joi.delivery.dto.response.ValidationErrorResponse.FieldError;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new FieldError(
                        error.getField(),
                        error.getDefaultMessage(),
                        error.getRejectedValue()
                ))
                .toList();

        log.warn("Request body validation failed: {} errors", fieldErrors.size());

        return ValidationErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                "Request body validation failed",
                fieldErrors
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleConstraintViolation(ConstraintViolationException ex) {
        List<FieldError> fieldErrors = ex.getConstraintViolations().stream()
                .map(violation -> {
                    String field = extractFieldName(violation);
                    return new FieldError(
                            field,
                            violation.getMessage(),
                            violation.getInvalidValue()
                    );
                })
                .toList();

        log.warn("Constraint violation: {} errors", fieldErrors.size());

        return ValidationErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                "Request parameter validation failed",
                fieldErrors
        );
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleMissingServletRequestParameter(MissingServletRequestParameterException ex) {
        String field = ex.getParameterName();
        String detail = "Required parameter '" + field + "' is missing";

        log.warn("Missing required parameter: {}", field);

        return ValidationErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                detail,
                List.of(new FieldError(field, "Parameter is required", null))
        );
    }

    private String extractFieldName(ConstraintViolation<?> violation) {
        String path = violation.getPropertyPath().toString();
        int lastDot = path.lastIndexOf('.');
        return lastDot > 0 ? path.substring(lastDot + 1) : path;
    }
}