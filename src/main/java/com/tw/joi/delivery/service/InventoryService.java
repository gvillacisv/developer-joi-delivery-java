package com.tw.joi.delivery.service;

import com.tw.joi.delivery.domain.GroceryProduct;
import com.tw.joi.delivery.dto.response.InventoryHealth;
import com.tw.joi.delivery.dto.response.InventoryHealthStatus;
import com.tw.joi.delivery.seedData.SeedData;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class InventoryService {

    private static final Map<InventoryHealthStatus, Function<List<GroceryProduct>, Boolean>> HEALTH_CHECKS = 
            new LinkedHashMap<>(Map.of(
                    InventoryHealthStatus.OUT_OF_STOCK, products -> products.stream()
                            .anyMatch(p -> p.getAvailableStock() == 0),
                    InventoryHealthStatus.LOW_STOCK, products -> products.stream()
                            .anyMatch(p -> p.getAvailableStock() > 0 && p.getAvailableStock() <= p.getThreshold())
            ));

    public InventoryHealth getStoreHealth(String storeId) {
        List<GroceryProduct> storeProducts = SeedData.groceryProducts.stream()
                .filter(p -> p.getStore().getOutletId().equals(storeId))
                .toList();

        if (storeProducts.isEmpty()) {
            return null;
        }

        InventoryHealthStatus status = HEALTH_CHECKS.keySet().stream()
                .filter(healthStatus -> HEALTH_CHECKS.get(healthStatus).apply(storeProducts))
                .findFirst()
                .orElse(InventoryHealthStatus.HEALTHY);

        return new InventoryHealth(storeId, status.name());
    }
}