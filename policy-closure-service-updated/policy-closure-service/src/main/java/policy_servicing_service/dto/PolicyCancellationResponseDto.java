package policy_servicing_service.dto;

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
public class PolicyCancellationResponseDto {

    private Long cancellationId;

    private Long policyId;

    private LocalDate cancellationDate;

    private String cancellationReason;

    private BigDecimal refundAmount;

    private Long approvedBy;
    private String cancellationRequestNumber;
    private String requestStatus;
    private String decisionRemarks;
    private java.time.LocalDateTime decisionDate;
}