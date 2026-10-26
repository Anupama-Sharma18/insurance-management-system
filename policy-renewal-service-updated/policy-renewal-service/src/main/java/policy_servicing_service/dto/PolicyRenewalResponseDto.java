package policy_servicing_service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import policy_servicing_service.enums.RenewalStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PolicyRenewalResponseDto {
    private Long renewalId;
    private Long policyId;
    private LocalDate renewalDate;
    private LocalDate newExpiryDate;
    private BigDecimal premiumAmount;
    private RenewalStatus renewalStatus;
    private boolean reminderSent;
    private boolean customerConfirmed;
    private boolean paymentVerified;
}
