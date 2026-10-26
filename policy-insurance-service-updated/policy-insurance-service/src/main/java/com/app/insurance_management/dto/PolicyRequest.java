
package com.app.insurance_management.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record PolicyRequest(

        @NotNull(message = "Quote ID is required")
        @Positive(message = "Quote ID must be positive")
        Long quoteId,

        @NotNull(message = "Customer ID is required")
        @Positive(message = "Customer ID must be positive")
        Long customerId,

        @NotNull(message = "Product ID is required")
        @Positive(message = "Product ID must be positive")
        Long productId,

        @NotNull(message = "Effective date is required")
        @FutureOrPresent(message = "Effective date cannot be in the past")
        LocalDate effectiveDate,

        @NotNull(message = "Expiry date is required")
        LocalDate expiryDate
) {
}
