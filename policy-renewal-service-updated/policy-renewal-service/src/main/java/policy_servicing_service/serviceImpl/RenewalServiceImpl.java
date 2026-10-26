package policy_servicing_service.serviceImpl;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import policy_servicing_service.dto.PolicyDetailsDto;
import policy_servicing_service.dto.PolicyRenewalRequestDto;
import policy_servicing_service.dto.PolicyRenewalResponseDto;
import policy_servicing_service.entity.Renewal;
import policy_servicing_service.enums.RenewalStatus;
import policy_servicing_service.exception.ResourceNotFoundException;
import policy_servicing_service.repository.RenewalRepository;
import policy_servicing_service.service.RenewalService;
import policy_servicing_service.service.PolicyDataService;

@Service
@RequiredArgsConstructor
public class RenewalServiceImpl implements RenewalService {
    private final RenewalRepository renewalRepository;
    private final PolicyDataService policyDataService;

    @Override
    @Transactional
    public PolicyRenewalResponseDto createRenewal(PolicyRenewalRequestDto requestDto) {
        PolicyDetailsDto policy = policyDataService.getPolicyById(requestDto.getPolicyId());
        if (policy == null || !requestDto.getPolicyId().equals(policy.getPolicyId())) {
            throw new ResourceNotFoundException("Policy not found: " + requestDto.getPolicyId());
        }
        if (policy.getPolicyStatus() == null || !"ACTIVE".equalsIgnoreCase(policy.getPolicyStatus())) {
            throw new IllegalStateException("Policy must be ACTIVE to request renewal");
        }
        if (policy.getExpiryDate() == null) {
            throw new IllegalStateException("Policy expiry date is not available");
        }
        if (policy.getExpiryDate().isBefore(LocalDate.now())) {
            throw new IllegalStateException("Policy is expired; renewal eligibility requires review");
        }
        if (!requestDto.getNewExpiryDate().isAfter(policy.getExpiryDate())) {
            throw new IllegalArgumentException("New expiry date must be after the current policy expiry date");
        }
        if (requestDto.getRenewalDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Renewal date cannot be in the past");
        }

        Renewal renewal = new Renewal();
        renewal.setPolicyId(requestDto.getPolicyId());
        renewal.setRenewalDate(requestDto.getRenewalDate());
        renewal.setNewExpiryDate(requestDto.getNewExpiryDate());
        renewal.setPremiumAmount(requestDto.getPremiumAmount());
        renewal.setRenewalStatus(RenewalStatus.PENDING);
        return toResponse(renewalRepository.save(renewal));
    }

    @Override
    public List<PolicyRenewalResponseDto> getAllRenewals() {
        return renewalRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public PolicyRenewalResponseDto getRenewalById(Long renewalId) {
        return toResponse(findRenewal(renewalId));
    }

    @Override
    public List<PolicyRenewalResponseDto> getRenewalsByPolicyId(Long policyId) {
        return renewalRepository.findByPolicyId(policyId).stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public PolicyRenewalResponseDto sendReminder(Long renewalId) {
        Renewal renewal = findRenewal(renewalId);
        if (renewal.getRenewalStatus() == RenewalStatus.RENEWED) {
            throw new IllegalStateException("Policy is already renewed");
        }
        renewal.setReminderSent(true);
        return toResponse(renewalRepository.save(renewal));
    }

    @Override
    @Transactional
    public PolicyRenewalResponseDto confirmCustomer(Long renewalId) {
        Renewal renewal = findRenewal(renewalId);
        if (!renewal.isReminderSent()) {
            throw new IllegalStateException("A renewal reminder must be sent before customer confirmation");
        }
        if (renewal.getRenewalStatus() != RenewalStatus.PENDING) {
            throw new IllegalStateException("Only a PENDING renewal can be confirmed");
        }
        renewal.setCustomerConfirmed(true);
        return toResponse(renewalRepository.save(renewal));
    }

    @Override
    @Transactional
    public PolicyRenewalResponseDto verifyPayment(Long renewalId, boolean paymentSuccessful) {
        Renewal renewal = findRenewal(renewalId);
        if (!renewal.isCustomerConfirmed()) {
            throw new IllegalStateException("Customer confirmation is required before payment verification");
        }
        if (!paymentSuccessful) {
            throw new IllegalStateException("Payment was not successful");
        }
        renewal.setPaymentVerified(true);
        renewal.setRenewalStatus(RenewalStatus.PAID);
        // TEST ONLY: update the in-memory policy so the complete flow can be tested without another service.
        policyDataService.renewPolicy(renewal.getPolicyId(), renewal.getNewExpiryDate());
        renewal.setRenewalStatus(RenewalStatus.RENEWED);
        return toResponse(renewalRepository.save(renewal));
    }

    private Renewal findRenewal(Long renewalId) {
        return renewalRepository.findById(renewalId)
                .orElseThrow(() -> new ResourceNotFoundException("Renewal not found: " + renewalId));
    }

    private PolicyRenewalResponseDto toResponse(Renewal r) {
        return PolicyRenewalResponseDto.builder()
                .renewalId(r.getRenewalId())
                .policyId(r.getPolicyId())
                .renewalDate(r.getRenewalDate())
                .newExpiryDate(r.getNewExpiryDate())
                .premiumAmount(r.getPremiumAmount())
                .renewalStatus(r.getRenewalStatus())
                .reminderSent(r.isReminderSent())
                .customerConfirmed(r.isCustomerConfirmed())
                .paymentVerified(r.isPaymentVerified())
                .build();
    }
}
