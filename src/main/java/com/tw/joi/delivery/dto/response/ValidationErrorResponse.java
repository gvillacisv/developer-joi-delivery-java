package com.tw.joi.delivery.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

/**
 * RFC 7807 Problem Detail for HTTP APIs
 * @see https://www.rfc-editor.org/rfc/rfc7807
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ValidationErrorResponse(
    URI type,
    String title,
    Integer status,
    String detail,
    URI instance,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime timestamp,
    List<FieldError> extensions
) {
    /**
     * Field-level validation error (RFC 7807 extension)
     */
    public record FieldError(
        String field,
        String message,
        Object rejectedValue
    ) {}

    /**
     * Factory for validation errors
     */
    public static ValidationErrorResponse of(int status, String title, String detail, List<FieldError> fieldErrors) {
        return new ValidationErrorResponse(
            URI.create("urn:problem:validation"),
            title,
            status,
            detail,
            null,
            LocalDateTime.now(),
            fieldErrors
        );
    }

    /**
     * Simple validation error without field details
     */
    public static ValidationErrorResponse of(int status, String title, String detail) {
        return of(status, title, detail, null);
    }
}