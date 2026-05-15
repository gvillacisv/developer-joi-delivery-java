package com.tw.joi.delivery.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.tw.joi.delivery.domain.GroceryProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("should return product when productId and outletId match")
    void shouldReturnProductWhenProductIdAndOutletIdMatch() {
        // Act
        GroceryProduct result = productService.getProduct("product101", "store101");

        // Assert
        assertNotNull(result);
        assertEquals("product101", result.getProductId());
    }

    @Test
    @DisplayName("should return null when productId does not match")
    void shouldReturnNullWhenProductIdDoesNotMatch() {
        // Act
        GroceryProduct result = productService.getProduct("unknownProduct", "store101");

        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("should return null when outletId does not match")
    void shouldReturnNullWhenOutletIdDoesNotMatch() {
        // Act
        GroceryProduct result = productService.getProduct("product101", "unknownStore");

        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("should return null when both productId and outletId do not match")
    void shouldReturnNullWhenBothDoNotMatch() {
        // Act
        GroceryProduct result = productService.getProduct("unknownProduct", "unknownStore");

        // Assert
        assertNull(result);
    }
}