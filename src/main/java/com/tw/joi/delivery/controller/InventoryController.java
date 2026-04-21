package com.tw.joi.delivery.controller;

import com.tw.joi.delivery.dto.response.InventoryHealth;
import com.tw.joi.delivery.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/health")
    public ResponseEntity<InventoryHealth> fetchStoreInventoryHealth(@RequestParam(name = "storeId") String storeId) {
        InventoryHealth health = inventoryService.getStoreHealth(storeId);
        if (health == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(health);
    }
}