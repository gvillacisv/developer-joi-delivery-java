package com.tw.joi.delivery.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AddProductRequest(
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9_-]{3,50}$")
    String userId,

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9_-]{3,50}$")
    String outletId,

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9_-]{3,50}$")
    String productId
) {}