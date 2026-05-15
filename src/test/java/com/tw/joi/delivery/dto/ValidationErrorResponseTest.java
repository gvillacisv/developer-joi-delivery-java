package com.tw.joi.delivery.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.tw.joi.delivery.dto.response.ValidationErrorResponse;
import com.tw.joi.delivery.dto.response.ValidationErrorResponse.FieldError;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ValidationErrorResponseTest {

    @Nested
    @DisplayName("factory methods")
    class FactoryMethodsTests {

        @Test
        @DisplayName("should create error response with field errors")
        void shouldCreateErrorResponseWithFieldErrors() {
            // Arrange
            List<FieldError> errors = List.of(
                new FieldError("userId", "must not be blank", null)
            );

            // Act
            ValidationErrorResponse response = ValidationErrorResponse.of(
                400, "Bad Request", "Validation failed", errors);

            // Assert
            assertNotNull(response);
            assertEquals(400, response.status());
            assertEquals("Bad Request", response.title());
            assertEquals(1, response.extensions().size());
        }

        @Test
        @DisplayName("should create error response with message only")
        void shouldCreateErrorResponseWithMessageOnly() {
            // Act
            ValidationErrorResponse response = ValidationErrorResponse.of(
                400, "Bad Request", "Validation failed");

            // Assert
            assertNotNull(response);
            assertEquals(400, response.status());
            assertEquals("Bad Request", response.title());
        }
    }
}