package policy_servicing_service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PolicyRenewalRequestDto {
    @NotNull(message = "Policy ID is required")
    @Positive(message = "Policy ID must be positive")
    private Long policyId;

    @NotNull(message = "Renewal date is required")
    private LocalDate renewalDate;

    @NotNull(message = "New expiry date is required")
    private LocalDate newExpiryDate;

    @NotNull(message = "Premium amount is required")
    @DecimalMin(value = "0.01", message = "Premium amount must be greater than zero")
    private BigDecimal premiumAmount;
}
