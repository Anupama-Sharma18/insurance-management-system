
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
public class EndorsementRequestDto {

    @NotNull(message = "Request ID is required")
    @Positive(message = "Request ID must be positive")
    private Long requestId;

    @NotBlank(message = "Endorsement type is required")
    private String endorsementType;

    private String previousValue;

    @NotBlank(message = "Updated value is required")
    private String updatedValue;
}
