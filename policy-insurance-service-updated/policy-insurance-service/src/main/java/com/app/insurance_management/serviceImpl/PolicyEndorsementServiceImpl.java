
        package com.app.insurance_management.serviceImpl;

import com.app.insurance_management.dto.PolicyEndorsementResponse;
import com.app.insurance_management.entity.Policy;
import com.app.insurance_management.entity.PolicyEndorsement;
import com.app.insurance_management.exception.ResourceNotFoundException;
import com.app.insurance_management.repository.PolicyEndorsementRepository;
import com.app.insurance_management.repository.PolicyRepository;
import com.app.insurance_management.service.PolicyEndorsementService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PolicyEndorsementServiceImpl
        implements PolicyEndorsementService {

    private final PolicyEndorsementRepository endorsementRepository;
    private final PolicyRepository policyRepository;

    @Override
    public PolicyEndorsementResponse createEndorsement(
            Long policyId,
            PolicyEndorsement endorsement) {

        if (endorsement == null) {
            throw new IllegalArgumentException(
                    "Endorsement details are required"
            );
        }

        Policy policy = findPolicy(policyId);

        if (endorsement.getEndorsementType() == null
                || endorsement.getEndorsementType().isBlank()) {
            throw new IllegalArgumentException(
                    "Endorsement type is required"
            );
        }

        endorsement.setEndorsementId(null);
        endorsement.setPolicy(policy);
        endorsement.setApprovalStatus("PENDING");
        endorsement.setApprovedBy(null);
        endorsement.setRejectionReason(null);
        endorsement.setEndorsementDate(LocalDateTime.now());

        PolicyEndorsement saved =
                endorsementRepository.save(endorsement);

        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PolicyEndorsementResponse> getEndorsementsByPolicyId(
            Long policyId) {

        findPolicy(policyId);

        return endorsementRepository
                .findByPolicy_PolicyId(policyId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PolicyEndorsementResponse getEndorsementById(
            Long endorsementId) {

        return mapToResponse(findEndorsement(endorsementId));
    }

    @Override
    public PolicyEndorsementResponse approveEndorsement(
            Long endorsementId,
            Long approvedBy) {

        PolicyEndorsement endorsement =
                findEndorsement(endorsementId);

        validatePending(endorsement);

        if (approvedBy == null || approvedBy <= 0) {
            throw new IllegalArgumentException(
                    "Valid approver ID is required"
            );
        }

        endorsement.setApprovalStatus("APPROVED");
        endorsement.setApprovedBy(approvedBy);
        endorsement.setRejectionReason(null);

        PolicyEndorsement saved =
                endorsementRepository.save(endorsement);

        return mapToResponse(saved);
    }

    @Override
    public PolicyEndorsementResponse rejectEndorsement(
            Long endorsementId,
            Long rejectedBy,
            String reason) {

        PolicyEndorsement endorsement =
                findEndorsement(endorsementId);

        validatePending(endorsement);

        if (rejectedBy == null || rejectedBy <= 0) {
            throw new IllegalArgumentException(
                    "Valid reviewer ID is required"
            );
        }

        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException(
                    "Rejection reason is required"
            );
        }

        endorsement.setApprovalStatus("REJECTED");

        // Existing database schema has approved_by, not rejected_by.
        endorsement.setApprovedBy(rejectedBy);
        endorsement.setRejectionReason(reason.trim());

        PolicyEndorsement saved =
                endorsementRepository.save(endorsement);

        return mapToResponse(saved);
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

    private PolicyEndorsement findEndorsement(Long endorsementId) {

        if (endorsementId == null || endorsementId <= 0) {
            throw new IllegalArgumentException(
                    "Valid endorsement ID is required"
            );
        }

        return endorsementRepository.findById(endorsementId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Endorsement not found with ID: " + endorsementId
                ));
    }

    private void validatePending(PolicyEndorsement endorsement) {

        if (!"PENDING".equals(endorsement.getApprovalStatus())) {
            throw new IllegalStateException(
                    "Only pending endorsements can be approved or rejected"
            );
        }
    }

    private PolicyEndorsementResponse mapToResponse(
            PolicyEndorsement endorsement) {

        return new PolicyEndorsementResponse(
                endorsement.getEndorsementId(),
                endorsement.getPolicy().getPolicyId(),
                endorsement.getEndorsementType(),
                endorsement.getOldValue(),
                endorsement.getNewValue(),
                endorsement.getApprovalStatus(),
                endorsement.getApprovedBy(),
                endorsement.getRejectionReason(),
                endorsement.getEndorsementDate()
        );
    }
}
