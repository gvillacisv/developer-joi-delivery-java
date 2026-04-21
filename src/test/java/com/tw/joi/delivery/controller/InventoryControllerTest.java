package com.tw.joi.delivery.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.tw.joi.delivery.dto.response.InventoryHealth;
import com.tw.joi.delivery.service.InventoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(InventoryController.class)
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InventoryService inventoryService;

    @Test
    void shouldReturnHealthStatusForStore() throws Exception {
        String healthUrl = "/inventory/health";
        String storeId = "store101";

        when(inventoryService.getStoreHealth(storeId))
            .thenReturn(new InventoryHealth(storeId, "HEALTHY"));

        mockMvc.perform(MockMvcRequestBuilders.get(healthUrl)
                            .param("storeId", storeId)
                            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.storeId").value("store101"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("HEALTHY"));
    }

    @Test
    void shouldReturn404WhenStoreNotFound() throws Exception {
        String healthUrl = "/inventory/health";
        String storeId = "unknownStore";

        when(inventoryService.getStoreHealth(storeId)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get(healthUrl)
                            .param("storeId", storeId)
                            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }
}