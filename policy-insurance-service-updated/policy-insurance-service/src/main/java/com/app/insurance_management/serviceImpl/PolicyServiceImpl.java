
package com.app.insurance_management.serviceImpl;

import com.app.insurance_management.dto.PolicyRequest;
import com.app.insurance_management.dto.PolicyResponse;
import com.app.insurance_management.entity.Policy;
import com.app.insurance_management.enums.PolicyStatus;
import com.app.insurance_management.exception.ResourceNotFoundException;
import com.app.insurance_management.repository.PolicyRepository;
import com.app.insurance_management.service.PolicyHistoryService;
import com.app.insurance_management.service.PolicyService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PolicyServiceImpl implements PolicyService {

    private final PolicyRepository policyRepository;
    private final PolicyHistoryService policyHistoryService;

    @Override
    public PolicyResponse createPolicy(PolicyRequest request) {

        validateRequest(request);

        Policy policy = new Policy();

        policy.setQuoteId(request.quoteId());
        policy.setCustomerId(request.customerId());
        policy.setProductId(request.productId());
        policy.setIssueDate(LocalDate.now());
        policy.setEffectiveDate(request.effectiveDate());
        policy.setExpiryDate(request.expiryDate());

        policy.setPolicyStatus(PolicyStatus.PENDING_PAYMENT);

        Policy savedPolicy = policyRepository.save(policy);

        savedPolicy.setPolicyNumber(
                "POL-" + LocalDate.now().getYear()
                        + "-" + String.format(
                        "%06d",
                        savedPolicy.getPolicyId()
                )
        );

        savedPolicy = policyRepository.save(savedPolicy);

        policyHistoryService.saveHistory(
                savedPolicy.getPolicyId(),
                "POLICY_CREATED",
                null,
                "Policy created successfully; pending payment"
        );

        return mapToResponse(savedPolicy);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PolicyResponse> getAllPolicies() {

        return policyRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PolicyResponse getPolicyById(Long policyId) {

        return mapToResponse(findPolicy(policyId));
    }

    @Override
    public PolicyResponse updatePolicy(
            Long policyId,
            PolicyRequest request) {

        validateRequest(request);

        Policy policy = findPolicy(policyId);

        if (policy.getPolicyStatus() != PolicyStatus.DRAFT) {
            throw new IllegalStateException(
                    "Only draft policies can be updated"
            );
        }

        policy.setQuoteId(request.quoteId());
        policy.setCustomerId(request.customerId());
        policy.setProductId(request.productId());
        policy.setEffectiveDate(request.effectiveDate());
        policy.setExpiryDate(request.expiryDate());

        Policy updatedPolicy = policyRepository.save(policy);

        policyHistoryService.saveHistory(
                updatedPolicy.getPolicyId(),
                "POLICY_UPDATED",
                null,
                "Draft policy updated"
        );

        return mapToResponse(updatedPolicy);
    }

    @Override
    public void cancelPolicy(Long policyId) {

        Policy policy = findPolicy(policyId);

        if (policy.getPolicyStatus() == PolicyStatus.CANCELLED) {
            throw new IllegalStateException(
                    "Policy is already cancelled"
            );
        }

        if (policy.getPolicyStatus() == PolicyStatus.CLOSED
                || policy.getPolicyStatus() == PolicyStatus.EXPIRED) {

            throw new IllegalStateException(
                    "Closed or expired policies cannot be cancelled"
            );
        }

        policy.setPolicyStatus(PolicyStatus.CANCELLED);
        policyRepository.save(policy);

        saveStatusHistory(
                policy,
                "POLICY_CANCELLED",
                "Policy cancelled successfully"
        );
    }

    @Override
    public PolicyResponse activatePolicy(Long policyId) {

        Policy policy = findPolicy(policyId);

        if (policy.getPolicyStatus() != PolicyStatus.PENDING_PAYMENT) {
            throw new IllegalStateException(
                    "Only policies pending payment can be activated"
            );
        }

        policy.setPolicyStatus(PolicyStatus.ACTIVE);

        Policy updatedPolicy = policyRepository.save(policy);

        saveStatusHistory(
                updatedPolicy,
                "POLICY_ACTIVATED",
                "Policy activated successfully"
        );

        return mapToResponse(updatedPolicy);
    }

    @Override
    public PolicyResponse suspendPolicy(Long policyId) {

        Policy policy = findPolicy(policyId);

        if (policy.getPolicyStatus() != PolicyStatus.ACTIVE) {
            throw new IllegalStateException(
                    "Only active policies can be suspended"
            );
        }

        policy.setPolicyStatus(PolicyStatus.SUSPENDED);

        Policy updatedPolicy = policyRepository.save(policy);

        saveStatusHistory(
                updatedPolicy,
                "POLICY_SUSPENDED",
                "Policy suspended successfully"
        );

        return mapToResponse(updatedPolicy);
    }

    @Override
    public PolicyResponse expirePolicy(Long policyId) {

        Policy policy = findPolicy(policyId);

        if (policy.getPolicyStatus() == PolicyStatus.CANCELLED
                || policy.getPolicyStatus() == PolicyStatus.CLOSED) {

            throw new IllegalStateException(
                    "Cancelled or closed policies cannot be expired"
            );
        }

        if (policy.getPolicyStatus() == PolicyStatus.EXPIRED) {
            throw new IllegalStateException(
                    "Policy is already expired"
            );
        }

        if (policy.getExpiryDate() == null
                || policy.getExpiryDate().isAfter(LocalDate.now())) {

            throw new IllegalStateException(
                    "Policy expiry date has not been reached"
            );
        }

        policy.setPolicyStatus(PolicyStatus.EXPIRED);

        Policy updatedPolicy = policyRepository.save(policy);

        saveStatusHistory(
                updatedPolicy,
                "POLICY_EXPIRED",
                "Policy expired successfully"
        );

        return mapToResponse(updatedPolicy);
    }

    // Dedicated endpoint logic for Policy Renewal microservice.
    // Existing policy APIs remain unchanged.
    @Override
    public PolicyResponse renewPolicyExpiry(
            Long policyId,
            LocalDate newExpiryDate) {

        Policy policy = findPolicy(policyId);

        if (policy.getPolicyStatus() != PolicyStatus.ACTIVE) {
            throw new IllegalStateException(
                    "Only ACTIVE policies can be renewed"
            );
        }

        LocalDate today = LocalDate.now();
        LocalDate currentExpiry = policy.getExpiryDate();

        if (currentExpiry == null) {
            throw new IllegalStateException(
                    "Current policy expiry date is missing"
            );
        }

        if (!currentExpiry.isAfter(today)) {
            throw new IllegalStateException(
                    "Expired policies cannot be renewed"
            );
        }

        if (newExpiryDate == null
                || !newExpiryDate.isAfter(currentExpiry)) {

            throw new IllegalArgumentException(
                    "New expiry date must be after the current expiry date"
            );
        }

        policy.setExpiryDate(newExpiryDate);

        Policy updatedPolicy = policyRepository.save(policy);

        policyHistoryService.saveHistory(
                updatedPolicy.getPolicyId(),
                "POLICY_RENEWED",
                null,
                "Policy expiry updated from "
                        + currentExpiry
                        + " to "
                        + newExpiryDate
        );

        return mapToResponse(updatedPolicy);
    }

    private Policy findPolicy(Long policyId) {

        if (policyId == null || policyId <= 0) {
            throw new IllegalArgumentException(
                    "Valid policy ID is required"
            );
        }

        return policyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Policy not found with ID: " + policyId
                ));
    }

    private void validateRequest(PolicyRequest request) {

        if (request == null) {
            throw new IllegalArgumentException(
                    "Policy request is required"
            );
        }

        if (request.quoteId() == null || request.quoteId() <= 0) {
            throw new IllegalArgumentException(
                    "Valid quote ID is required"
            );
        }

        if (request.customerId() == null || request.customerId() <= 0) {
            throw new IllegalArgumentException(
                    "Valid customer ID is required"
            );
        }

        if (request.productId() == null || request.productId() <= 0) {
            throw new IllegalArgumentException(
                    "Valid product ID is required"
            );
        }

        if (request.effectiveDate() == null
                || request.expiryDate() == null) {

            throw new IllegalArgumentException(
                    "Effective date and expiry date are required"
            );
        }

        if (request.effectiveDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Effective date cannot be in the past"
            );
        }

        if (!request.expiryDate().isAfter(request.effectiveDate())) {
            throw new IllegalArgumentException(
                    "Expiry date must be after effective date"
            );
        }
    }

    private void saveStatusHistory(
            Policy policy,
            String action,
            String remarks) {

        policyHistoryService.saveHistory(
                policy.getPolicyId(),
                action,
                null,
                remarks
        );
    }

    private PolicyResponse mapToResponse(Policy policy) {

        return new PolicyResponse(
                policy.getPolicyId(),
                policy.getPolicyNumber(),
                policy.getQuoteId(),
                policy.getCustomerId(),
                policy.getProductId(),
                policy.getIssueDate(),
                policy.getEffectiveDate(),
                policy.getExpiryDate(),
                policy.getPolicyStatus()
        );
    }
}
