package com.tw.joi.delivery.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.tw.joi.delivery.domain.User;
import com.tw.joi.delivery.seedData.SeedData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("should return user when userId exists")
    void shouldReturnUserWhenUserIdExists() {
        // Act
        User result = userService.fetchUserById("user101");

        // Assert
        assertNotNull(result);
        assertEquals("user101", result.getUserId());
    }

    @Test
    @DisplayName("should return null when userId does not exist")
    void shouldReturnNullWhenUserIdDoesNotExist() {
        // Act
        User result = userService.fetchUserById("unknownUser");

        // Assert
        assertNull(result);
    }
}