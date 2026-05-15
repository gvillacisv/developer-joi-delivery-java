package com.tw.joi.delivery.domain;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.tw.joi.delivery.seedData.SeedData;
import java.util.HashSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GroceryStoreTest {

    @Test
    @DisplayName("should create GroceryStore from seed data")
    void shouldCreateGroceryStoreFromSeedData() {
        // Act - use existing seed data
        GroceryStore store = SeedData.store101;

        // Assert
        assertNotNull(store);
        assertNotNull(store.getInventory());
        assertNotNull(store.getOutletId());
    }

    @Test
    @DisplayName("should have inventory from seed data")
    void shouldHaveInventoryFromSeedData() {
        // Act
        GroceryStore store = SeedData.store101;

        // Assert
        assertNotNull(store.getInventory());
        // Inventory is populated from seed products
        assertNotNull(store.getInventory());
    }
}