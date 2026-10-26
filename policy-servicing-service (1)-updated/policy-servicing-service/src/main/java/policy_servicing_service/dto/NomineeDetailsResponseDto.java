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
public class NomineeDetailsResponseDto {

    private Long nomineeId;

    private Long policyId;

    private String nomineeName;

    private String relationship;

    private LocalDate dob;

    private BigDecimal sharePercentage;

    private String guardianName;
}