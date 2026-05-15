package com.tw.joi.delivery.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.tw.joi.delivery.service.CartService;

@WebMvcTest(CartController.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CartService cartService;

    @Test
    @DisplayName("should return 400 when required query param is missing")
    void shouldReturn400WhenRequiredQueryParamIsMissing() throws Exception {
        // Act & Assert - userId is missing (required param)
        mockMvc.perform(get("/cart/view"))
                .andExpect(status().isBadRequest());
    }
}