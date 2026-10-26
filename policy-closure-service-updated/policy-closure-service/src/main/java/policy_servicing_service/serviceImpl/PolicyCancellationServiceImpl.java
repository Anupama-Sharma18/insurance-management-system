package policy_servicing_service.serviceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import policy_servicing_service.dto.CancellationDecisionRequestDto;
import policy_servicing_service.dto.PolicyCancellationRequestDto;
import policy_servicing_service.dto.PolicyCancellationResponseDto;
import policy_servicing_service.dto.PolicyDetailsDto;
import policy_servicing_service.entity.PolicyCancellation;
import policy_servicing_service.exception.ResourceNotFoundException;
import policy_servicing_service.repository.PolicyCancellationRepository;
import policy_servicing_service.service.ClaimsEligibilityService;
import policy_servicing_service.service.PolicyCancellationService;
import policy_servicing_service.service.PolicyDataService;

@Service
@RequiredArgsConstructor
public class PolicyCancellationServiceImpl implements PolicyCancellationService {

    private final PolicyCancellationRepository cancellationRepository;
    private final PolicyDataService policyDataService;
    private final ClaimsEligibilityService claimsEligibilityService;

    @Override
    @Transactional
    public PolicyCancellationResponseDto cancelPolicy(
            PolicyCancellationRequestDto requestDto, Long policyId) {

        if (policyId == null || policyId <= 0) {
            throw new IllegalArgumentException("Policy ID must be positive");
        }

        // Step 1: Validate policy; do not cancel it yet.
        PolicyDetailsDto policy = policyDataService.getPolicyById(policyId);
        if (!"ACTIVE".equalsIgnoreCase(policy.getPolicyStatus())) {
            throw new IllegalStateException("Only ACTIVE policies can request cancellation");
        }

        if (cancellationRepository.existsByPolicyId(policyId)) {
            throw new IllegalStateException(
                    "A cancellation request already exists for policy: " + policyId);
        }

        PolicyCancellation cancellation = new PolicyCancellation();
        cancellation.setPolicyId(policyId);
        cancellation.setCancellationDate(LocalDate.now());
        cancellation.setCancellationReason(requestDto.getCancellationReason().trim());
        cancellation.setCancellationRequestNumber(
                "CAN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        cancellation.setRequestStatus("PENDING");
        cancellation.setRefundAmount(BigDecimal.ZERO); // Placeholder until refund rules are provided.
        cancellation.setApprovedBy(null);

        // Policy status remains ACTIVE until admin approval.
        return toResponse(cancellationRepository.save(cancellation));
    }

    @Override
    @Transactional
    public PolicyCancellationResponseDto approve(
            Long cancellationId, CancellationDecisionRequestDto request) {
        PolicyCancellation cancellation = findPending(cancellationId);

        // Step 2: Stop approval if an active claim exists.
        if (claimsEligibilityService.hasActiveClaims(cancellation.getPolicyId())) {
            throw new IllegalStateException(
                    "Cancellation cannot proceed while an active claim exists");
        }

        // Step 3: Update policy only after approval.
        policyDataService.cancelPolicy(cancellation.getPolicyId());
        cancellation.setApprovedBy(request.getApprovedBy());
        cancellation.setRequestStatus("APPROVED");
        cancellation.setDecisionRemarks(request.getRemarks());
        cancellation.setDecisionDate(LocalDateTime.now());

        return toResponse(cancellationRepository.save(cancellation));
    }

    @Override
    @Transactional
    public PolicyCancellationResponseDto reject(
            Long cancellationId, CancellationDecisionRequestDto request) {
        PolicyCancellation cancellation = findPending(cancellationId);
        cancellation.setApprovedBy(request.getApprovedBy());
        cancellation.setRequestStatus("REJECTED");
        cancellation.setDecisionRemarks(request.getRemarks());
        cancellation.setDecisionDate(LocalDateTime.now());
        return toResponse(cancellationRepository.save(cancellation));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PolicyCancellationResponseDto> getAllCancellations() {
        return cancellationRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PolicyCancellationResponseDto getCancellationById(Long cancellationId) {
        return toResponse(cancellationRepository.findById(cancellationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cancellation request not found: " + cancellationId)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PolicyCancellationResponseDto> getCancellationsByPolicyId(Long policyId) {
        return cancellationRepository.findByPolicyId(policyId).stream()
                .map(this::toResponse).toList();
    }

    private PolicyCancellation findPending(Long cancellationId) {
        PolicyCancellation cancellation = cancellationRepository.findById(cancellationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cancellation request not found: " + cancellationId));
        if (!"PENDING".equalsIgnoreCase(cancellation.getRequestStatus())) {
            throw new IllegalStateException("Only PENDING cancellation requests can be decided");
        }
        return cancellation;
    }

    private PolicyCancellationResponseDto toResponse(PolicyCancellation cancellation) {
        return PolicyCancellationResponseDto.builder()
                .cancellationId(cancellation.getCancellationId())
                .policyId(cancellation.getPolicyId())
                .cancellationDate(cancellation.getCancellationDate())
                .cancellationReason(cancellation.getCancellationReason())
                .refundAmount(cancellation.getRefundAmount())
                .approvedBy(cancellation.getApprovedBy())
                .cancellationRequestNumber(cancellation.getCancellationRequestNumber())
                .requestStatus(cancellation.getRequestStatus())
                .decisionRemarks(cancellation.getDecisionRemarks())
                .decisionDate(cancellation.getDecisionDate())
                .build();
    }
}
