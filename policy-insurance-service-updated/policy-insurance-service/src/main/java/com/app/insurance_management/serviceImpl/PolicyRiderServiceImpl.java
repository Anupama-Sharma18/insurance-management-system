
package com.app.insurance_management.serviceImpl;

import com.app.insurance_management.dto.PolicyRiderResponse;
import com.app.insurance_management.entity.Policy;
import com.app.insurance_management.entity.PolicyRider;
import com.app.insurance_management.exception.ResourceNotFoundException;
import com.app.insurance_management.repository.PolicyRepository;
import com.app.insurance_management.repository.PolicyRiderRepository;
import com.app.insurance_management.service.PolicyRiderService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PolicyRiderServiceImpl implements PolicyRiderService {

    private final PolicyRiderRepository policyRiderRepository;
    private final PolicyRepository policyRepository;

    public PolicyRiderResponse addRider(Long policyId, PolicyRider rider) {

        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Policy not found with ID: " + policyId
                ));

        if (rider.getRiderId() == null || rider.getRiderId() <= 0) {
            throw new IllegalArgumentException("Valid rider ID is required");
        }

        BigDecimal premium = rider.getRiderPremium();

        if (premium == null || premium.signum() < 0) {
            throw new IllegalArgumentException(
                    "Rider premium cannot be null or negative"
            );
        }

        rider.setPolicyRiderId(null);
        rider.setPolicy(policy);

        PolicyRider saved = policyRiderRepository.save(rider);

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<PolicyRiderResponse> getRidersByPolicyId(Long policyId) {

        if (!policyRepository.existsById(policyId)) {
            throw new ResourceNotFoundException(
                    "Policy not found with ID: " + policyId
            );
        }

        return policyRiderRepository.findByPolicy_PolicyId(policyId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PolicyRiderResponse getRiderById(Long policyRiderId) {

        PolicyRider rider = policyRiderRepository.findById(policyRiderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Policy rider not found with ID: " + policyRiderId
                ));

        return toResponse(rider);
    }

    public PolicyRiderResponse updateRider(
            Long policyRiderId,
            PolicyRider request) {

        PolicyRider existing = policyRiderRepository.findById(policyRiderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Policy rider not found with ID: " + policyRiderId
                ));

        if (request.getRiderPremium() == null
                || request.getRiderPremium().signum() < 0) {
            throw new IllegalArgumentException(
                    "Rider premium cannot be null or negative"
            );
        }

        existing.setRiderPremium(request.getRiderPremium());

        PolicyRider updated = policyRiderRepository.save(existing);

        return toResponse(updated);
    }

    public void removeRider(Long policyRiderId) {

        PolicyRider rider = policyRiderRepository.findById(policyRiderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Policy rider not found with ID: " + policyRiderId
                ));

        policyRiderRepository.delete(rider);
    }

    private PolicyRiderResponse toResponse(PolicyRider rider) {

        return new PolicyRiderResponse(
                rider.getPolicyRiderId(),
                rider.getPolicy().getPolicyId(),
                rider.getRiderId(),
                rider.getRiderPremium()
        );
    }
}
