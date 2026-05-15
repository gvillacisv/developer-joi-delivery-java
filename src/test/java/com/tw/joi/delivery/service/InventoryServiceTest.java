package com.tw.joi.delivery.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.tw.joi.delivery.dto.response.InventoryHealth;
import com.tw.joi.delivery.dto.response.InventoryHealthStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @InjectMocks
    private InventoryService inventoryService;

    @Test
    @DisplayName("should return HEALTHY status when store has sufficient stock")
    void shouldReturnHealthyStatusWhenStoreHasSufficientStock() {
        // Act
        InventoryHealth result = inventoryService.getStoreHealth("store101");

        // Assert
        assertNotNull(result);
        assertEquals("store101", result.storeId());
        // Based on seed data: availableStock=30, threshold=10
        // Since 30 > 10, should be HEALTHY (no OUT_OF_STOCK or LOW_STOCK condition met)
        assertEquals(InventoryHealthStatus.HEALTHY.name(), result.status());
    }

    @Test
    @DisplayName("should return null when store does not exist")
    void shouldReturnNullWhenStoreDoesNotExist() {
        // Act
        InventoryHealth result = inventoryService.getStoreHealth("unknownStore");

        // Assert
        assertNull(result);
    }
}