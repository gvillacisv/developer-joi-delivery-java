package com.tw.joi.delivery.controller;

import com.tw.joi.delivery.dto.response.InventoryHealth;
import com.tw.joi.delivery.service.InventoryService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
@Validated
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/health")
    public ResponseEntity<InventoryHealth> fetchStoreInventoryHealth(
            @RequestParam(name = "storeId")
            @NotBlank
            @Pattern(regexp = "^[a-zA-Z0-9_-]{3,50}$")
            String storeId) {
        InventoryHealth health = inventoryService.getStoreHealth(storeId);
        if (health == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(health);
    }
}