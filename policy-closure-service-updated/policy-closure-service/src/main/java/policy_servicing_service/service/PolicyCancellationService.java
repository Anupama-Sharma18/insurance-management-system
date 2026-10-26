package policy_servicing_service.service;

import java.util.List;
import policy_servicing_service.dto.CancellationDecisionRequestDto;
import policy_servicing_service.dto.PolicyCancellationRequestDto;
import policy_servicing_service.dto.PolicyCancellationResponseDto;

public interface PolicyCancellationService {
    PolicyCancellationResponseDto cancelPolicy(PolicyCancellationRequestDto requestDto, Long policyId);
    List<PolicyCancellationResponseDto> getAllCancellations();
    PolicyCancellationResponseDto getCancellationById(Long cancellationId);
    List<PolicyCancellationResponseDto> getCancellationsByPolicyId(Long policyId);
    PolicyCancellationResponseDto approve(Long cancellationId, CancellationDecisionRequestDto request);
    PolicyCancellationResponseDto reject(Long cancellationId, CancellationDecisionRequestDto request);
}
