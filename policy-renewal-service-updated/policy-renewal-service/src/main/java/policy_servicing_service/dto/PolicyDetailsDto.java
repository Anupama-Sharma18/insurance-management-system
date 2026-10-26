package policy_servicing_service.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PolicyDetailsDto {

    private Long policyId;

    @JsonAlias("policyNumber")
    private String policyNo;

    private Long quoteId;

    private Long customerId;

    private Long productId;

    private LocalDate issueDate;

    private LocalDate effectiveDate;

    private LocalDate expiryDate;

    private String policyStatus;
}