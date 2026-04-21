package com.tw.joi.delivery.service;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService {

    public Map<String, Object> getStoreHealth(String storeId) {
        return Map.of(
            "storeId", storeId,
            "status", "HEALTHY",
            "lowStockProducts", java.util.Collections.emptyList(),
            "outOfStockProducts", java.util.Collections.emptyList()
        );
    }
}