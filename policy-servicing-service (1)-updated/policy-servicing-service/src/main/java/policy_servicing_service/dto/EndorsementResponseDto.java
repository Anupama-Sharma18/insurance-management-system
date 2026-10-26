
package policy_servicing_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import policy_servicing_service.enums.ApprovalStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EndorsementResponseDto {

    private Long endorsementId;

    private Long requestId;

    private String endorsementNumber;

    private String endorsementType;

    private String previousValue;

    private String updatedValue;

    private ApprovalStatus approvalStatus;
}
