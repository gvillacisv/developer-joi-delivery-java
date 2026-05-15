package com.tw.joi.delivery.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.tw.joi.delivery.dto.response.InventoryHealth;
import com.tw.joi.delivery.dto.response.InventoryHealthStatus;
import com.tw.joi.delivery.seedData.SeedData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InventoryServiceAdditionalTest {

    @InjectMocks
    private InventoryService inventoryService;

    @Test
    @DisplayName("should return correct health status for store101")
    void shouldReturnCorrectHealthStatusForStore101() {
        // Act
        InventoryHealth result = inventoryService.getStoreHealth("store101");

        // Assert
        assertNotNull(result);
        // Seed data: availableStock=30, threshold=10
        // Since 30 > 10 and > 0, should be HEALTHY
        assertEquals("HEALTHY", result.status());
        assertEquals("store101", result.storeId());
    }

    @Test
    @DisplayName("should verify InventoryHealthStatus enum values")
    void shouldVerifyInventoryHealthStatusEnumValues() {
        // Assert - verify enum values
        assertEquals("HEALTHY", InventoryHealthStatus.HEALTHY.name());
        assertEquals("LOW_STOCK", InventoryHealthStatus.LOW_STOCK.name());
        assertEquals("OUT_OF_STOCK", InventoryHealthStatus.OUT_OF_STOCK.name());
    }
}