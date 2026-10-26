package policy_servicing_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CancellationDecisionRequestDto {
    @NotNull
    @Positive
    private Long approvedBy;

    @Size(max = 1000)
    private String remarks;
}
