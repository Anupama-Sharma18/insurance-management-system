
package com.app.insurance_management.serviceImpl;

import com.app.insurance_management.entity.Policy;
import com.app.insurance_management.entity.PolicyCoverage;
import com.app.insurance_management.exception.ResourceNotFoundException;
import com.app.insurance_management.repository.PolicyCoverageRepository;
import com.app.insurance_management.repository.PolicyRepository;
import com.app.insurance_management.service.PolicyCoverageService;

import lombok.RequiredArgsConstructor;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PolicyCoverageServiceImpl implements PolicyCoverageService {

    private final PolicyCoverageRepository policyCoverageRepository;
    private final PolicyRepository policyRepository;

    @Override
    public PolicyCoverage addCoverage(
            Long policyId,
            PolicyCoverage coverage) {

        if (policyId == null || policyId <= 0) {
            throw new IllegalArgumentException(
                    "Valid policy ID is required"
            );
        }

        if (coverage == null) {
            throw new IllegalArgumentException(
                    "Coverage details are required"
            );
        }

        Policy policy = findPolicy(policyId);

        if (coverage.getCoverageName() == null
                || coverage.getCoverageName().isBlank()) {
            throw new IllegalArgumentException(
                    "Coverage name is required"
            );
        }

        if (coverage.getSumInsured() == null
                || coverage.getSumInsured().signum() <= 0) {
            throw new IllegalArgumentException(
                    "Sum insured must be greater than zero"
            );
        }

        if (policy.getEffectiveDate() == null) {
            throw new IllegalStateException(
                    "Policy effective date is missing"
            );
        }

        if (coverage.getCoverageStartDate() == null) {
            throw new IllegalArgumentException(
                    "Coverage start date is required"
            );
        }

        if (!coverage.getCoverageStartDate()
                .isEqual(policy.getEffectiveDate())) {
            throw new IllegalArgumentException(
                    "Coverage start date mismatch. Request date: "
                            + coverage.getCoverageStartDate()
                            + ", Policy effective date: "
                            + policy.getEffectiveDate()
            );
        }

        if (policy.getExpiryDate() == null) {
            throw new IllegalStateException(
                    "Policy expiry date is missing"
            );
        }

        if (coverage.getCoverageEndDate() == null) {
            throw new IllegalArgumentException(
                    "Coverage end date is required"
            );
        }

        if (coverage.getCoverageEndDate()
                .isAfter(policy.getExpiryDate())
                || coverage.getCoverageEndDate()
                .isBefore(coverage.getCoverageStartDate())) {
            throw new IllegalArgumentException(
                    "Coverage end date must be between coverage start date "
                            + "and policy expiry date"
            );
        }

        // Do not accept a client-supplied coverage ID.
        coverage.setCoverageId(null);
        coverage.setPolicy(policy);

        return policyCoverageRepository.save(coverage);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PolicyCoverage> getCoveragesByPolicyId(Long policyId) {

        validateId(policyId, "policy");

        if (!policyRepository.existsById(policyId)) {
            throw new ResourceNotFoundException(
                    "Policy not found with ID: " + policyId
            );
        }

        List<PolicyCoverage> coverages =
                policyCoverageRepository.findByPolicy_PolicyId(policyId);

        // Initialize the Policy relationship while the transaction is open.
        coverages.forEach(coverage ->
                Hibernate.initialize(coverage.getPolicy())
        );

        return coverages;
    }

    @Override
    @Transactional(readOnly = true)
    public PolicyCoverage getCoverageById(Long coverageId) {

        validateId(coverageId, "coverage");

        PolicyCoverage coverage = policyCoverageRepository
                .findById(coverageId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Coverage not found with ID: " + coverageId
                ));

        // Initialize the Policy relationship before returning.
        Hibernate.initialize(coverage.getPolicy());

        return coverage;
    }

    @Override
    public PolicyCoverage updateCoverage(
            Long coverageId,
            PolicyCoverage request) {

        validateId(coverageId, "coverage");

        if (request == null) {
            throw new IllegalArgumentException(
                    "Coverage details are required"
            );
        }

        PolicyCoverage existing = getCoverageById(coverageId);

        if (request.getSumInsured() == null
                || request.getSumInsured().signum() <= 0) {
            throw new IllegalArgumentException(
                    "Sum insured must be greater than zero"
            );
        }

        if (request.getCoverageName() != null) {
            if (request.getCoverageName().isBlank()) {
                throw new IllegalArgumentException(
                        "Coverage name cannot be blank"
                );
            }

            existing.setCoverageName(request.getCoverageName());
        }

        existing.setSumInsured(request.getSumInsured());

        return policyCoverageRepository.save(existing);
    }

    @Override
    public void deleteCoverage(Long coverageId) {

        validateId(coverageId, "coverage");

        PolicyCoverage coverage = getCoverageById(coverageId);

        policyCoverageRepository.delete(coverage);
    }

    private Policy findPolicy(Long policyId) {

        validateId(policyId, "policy");

        return policyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Policy not found with ID: " + policyId
                ));
    }

    private void validateId(Long id, String resourceName) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "Valid " + resourceName + " ID is required"
            );
        }
    }
}
