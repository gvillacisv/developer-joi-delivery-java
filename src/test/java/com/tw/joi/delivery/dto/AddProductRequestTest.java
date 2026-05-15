package com.tw.joi.delivery.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.tw.joi.delivery.dto.request.AddProductRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AddProductRequestTest {

    @Test
    @DisplayName("should create AddProductRequest with all fields")
    void shouldCreateAddProductRequestWithAllFields() {
        // Act
        AddProductRequest request = new AddProductRequest("user101", "store101", "product101");

        // Assert
        assertNotNull(request);
        assertEquals("user101", request.userId());
        assertEquals("store101", request.outletId());
        assertEquals("product101", request.productId());
    }
}