package com.tw.joi.delivery.dto;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.tw.joi.delivery.domain.Cart;
import com.tw.joi.delivery.domain.GroceryProduct;
import com.tw.joi.delivery.domain.Product;
import com.tw.joi.delivery.dto.response.CartProductInfo;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CartProductInfoTest {

    @Test
    @DisplayName("should create CartProductInfo")
    void shouldCreateCartProductInfo() {
        // Arrange - use seed data
        GroceryProduct product = com.tw.joi.delivery.seedData.SeedData.groceryProducts.get(0);
        Cart cart = com.tw.joi.delivery.seedData.SeedData.cartForUsers.get("user101");

        // Act
        CartProductInfo info = new CartProductInfo(cart, product, product.getSellingPrice());

        // Assert
        assertNotNull(info);
        assertNotNull(info.cart());
        assertNotNull(info.product());
    }
}