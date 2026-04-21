package com.tw.joi.delivery.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.tw.joi.delivery.domain.Cart;
import com.tw.joi.delivery.domain.GroceryProduct;
import com.tw.joi.delivery.domain.User;
import com.tw.joi.delivery.dto.request.AddProductRequest;
import com.tw.joi.delivery.dto.response.CartProductInfo;
import com.tw.joi.delivery.service.CartService;
import java.math.BigDecimal;
import java.util.ArrayList;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(CartController.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CartService cartService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldAddTheRequestedProductToTheCart() throws Exception {
        String addProductUrl = "/cart/product";
        AddProductRequest addProductRequest = new AddProductRequest("user101", "store101", "product101");

        User user = User.builder()
            .userId("user101")
            .username("john")
            .build();
        Cart cart = Cart.builder()
            .cartId("cart101")
            .user(user)
            .products(new ArrayList<>())
            .build();
        GroceryProduct product = GroceryProduct.builder()
            .productId("product101")
            .productName("Milk")
            .sellingPrice(BigDecimal.valueOf(2.99))
            .mrp(BigDecimal.valueOf(3.99))
            .build();

        when(cartService.addProductToCartForUser(addProductRequest))
            .thenReturn(new CartProductInfo(cart, product, BigDecimal.valueOf(2.99)));

        ObjectWriter writer = objectMapper.writer();
        String requestBody = writer.writeValueAsString(addProductRequest);

        mockMvc.perform(MockMvcRequestBuilders.post(addProductUrl)
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.cart.cartId").value("cart101"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.product.productId").value("product101"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.sellingPrice").value(2.99));
    }

    @Test
    void shouldReturnTheCart() throws Exception {
        String viewCartUrl = "/cart/view";
        String userId = "user101";
        User user = User.builder()
            .userId("user101")
            .username("john")
            .build();
        Cart cart = Cart.builder()
            .cartId("cart101")
            .user(user)
            .products(new ArrayList<>())
            .build();
        when(cartService.getCartForUser(userId)).thenReturn(cart);

        mockMvc.perform(MockMvcRequestBuilders.get(viewCartUrl)
                            .param("userId", userId)
                            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.cartId", Is.is("cart101")));
    }

    @Test
    void shouldReturn404WhenUserNotFound() throws Exception {
        String viewCartUrl = "/cart/view";
        String userId = "unknownUser";
        when(cartService.getCartForUser(userId)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get(viewCartUrl)
                            .param("userId", userId)
                            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404WhenProductNotFound() throws Exception {
        String addProductUrl = "/cart/product";
        AddProductRequest addProductRequest = new AddProductRequest("user101", "store101", "invalidProduct");

        when(cartService.addProductToCartForUser(addProductRequest))
            .thenReturn(null);

        ObjectWriter writer = objectMapper.writer();
        String requestBody = writer.writeValueAsString(addProductRequest);

        mockMvc.perform(MockMvcRequestBuilders.post(addProductUrl)
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }
}