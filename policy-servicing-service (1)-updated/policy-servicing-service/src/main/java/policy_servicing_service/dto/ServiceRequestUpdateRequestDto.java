package policy_servicing_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import policy_servicing_service.enums.ServiceRequestStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRequestUpdateRequestDto {

    @NotNull(message = "Status is required")
    private ServiceRequestStatus status;

    @NotNull(message = "Action by is required")
    @Positive(message = "Action by must be positive")
    private Long actionBy;

    private String remarks;
}