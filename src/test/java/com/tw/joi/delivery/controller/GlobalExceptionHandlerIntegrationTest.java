package com.tw.joi.delivery.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tw.joi.delivery.dto.request.AddProductRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class GlobalExceptionHandlerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("should return 400 with validation errors for missing query param")
    void shouldReturn400WithValidationErrorsForMissingQueryParam() throws Exception {
        mockMvc.perform(get("/cart/view"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.status").exists());
    }

    @Test
    @DisplayName("should return 400 with validation errors for empty query param")
    void shouldReturn400WithValidationErrorsForEmptyQueryParam() throws Exception {
        mockMvc.perform(get("/cart/view").param("userId", ""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").exists());
    }

    @Test
    @DisplayName("should return 400 with validation errors for inventory health")
    void shouldReturn400WithValidationErrorsForInventoryHealth() throws Exception {
        mockMvc.perform(get("/inventory/health"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").exists());
    }

    @Test
    @DisplayName("should return 400 for invalid pattern in userId")
    void shouldReturn400ForInvalidPatternInUserId() throws Exception {
        mockMvc.perform(get("/cart/view").param("userId", "ab@"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").exists());
    }

    @Test
    @DisplayName("should return 400 with field errors for request body validation")
    void shouldReturn400WithFieldErrorsForRequestBodyValidation() throws Exception {
        // Send request with null required fields
        AddProductRequest request = new AddProductRequest(null, null, null);
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/cart/product")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.extensions").exists());
    }
}