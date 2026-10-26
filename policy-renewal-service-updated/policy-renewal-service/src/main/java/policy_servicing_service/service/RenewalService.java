package policy_servicing_service.service;

import java.util.List;
import policy_servicing_service.dto.PolicyRenewalRequestDto;
import policy_servicing_service.dto.PolicyRenewalResponseDto;

public interface RenewalService {
    PolicyRenewalResponseDto createRenewal(PolicyRenewalRequestDto requestDto);
    List<PolicyRenewalResponseDto> getAllRenewals();
    PolicyRenewalResponseDto getRenewalById(Long renewalId);
    List<PolicyRenewalResponseDto> getRenewalsByPolicyId(Long policyId);
    PolicyRenewalResponseDto sendReminder(Long renewalId);
    PolicyRenewalResponseDto confirmCustomer(Long renewalId);
    PolicyRenewalResponseDto verifyPayment(Long renewalId, boolean paymentSuccessful);
}
