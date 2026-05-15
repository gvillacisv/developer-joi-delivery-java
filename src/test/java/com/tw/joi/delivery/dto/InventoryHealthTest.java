package com.tw.joi.delivery.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.tw.joi.delivery.dto.response.InventoryHealth;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class InventoryHealthTest {

    @Test
    @DisplayName("should create InventoryHealth with store id and status")
    void shouldCreateInventoryHealthWithStoreIdAndStatus() {
        // Act
        InventoryHealth health = new InventoryHealth("store101", "HEALTHY");

        // Assert
        assertNotNull(health);
        assertEquals("store101", health.storeId());
        assertEquals("HEALTHY", health.status());
    }
}