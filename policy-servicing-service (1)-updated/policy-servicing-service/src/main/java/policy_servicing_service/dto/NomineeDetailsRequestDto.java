package policy_servicing_service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NomineeDetailsRequestDto {

    @NotNull(message = "Policy ID is required")
    @Positive(message = "Policy ID must be positive")
    private Long policyId;

    @NotBlank(message = "Nominee name is required")
    private String nomineeName;

    @NotBlank(message = "Relationship is required")
    private String relationship;

    @NotNull(message = "Date of birth is required")
    private LocalDate dob;

    @NotNull(message = "Share percentage is required")
    @DecimalMin(value = "0.01", message = "Share percentage must be greater than zero")
    @DecimalMax(value = "100.00", message = "Share percentage cannot exceed 100")
    private BigDecimal sharePercentage;

    private String guardianName;
}