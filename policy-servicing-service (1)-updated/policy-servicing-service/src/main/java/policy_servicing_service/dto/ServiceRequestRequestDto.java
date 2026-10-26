
package policy_servicing_service.dto;

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
public class ServiceRequestRequestDto {

    @NotNull(message = "Policy ID is required")
    @Positive(message = "Policy ID must be positive")
    private Long policyId;

    @NotNull(message = "Customer ID is required")
    @Positive(message = "Customer ID must be positive")
    private Long customerId;

    @NotBlank(message = "Request type is required")
    private String requestType;

    @NotBlank(message = "Request details are required")
    private String requestDetails;
}
