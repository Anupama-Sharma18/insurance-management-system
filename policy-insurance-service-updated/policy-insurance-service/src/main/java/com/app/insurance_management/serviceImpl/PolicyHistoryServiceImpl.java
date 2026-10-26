
        package com.app.insurance_management.serviceImpl;

import com.app.insurance_management.dto.PolicyHistoryResponse;
import com.app.insurance_management.entity.Policy;
import com.app.insurance_management.entity.PolicyHistory;
import com.app.insurance_management.exception.ResourceNotFoundException;
import com.app.insurance_management.repository.PolicyHistoryRepository;
import com.app.insurance_management.repository.PolicyRepository;
import com.app.insurance_management.service.PolicyHistoryService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PolicyHistoryServiceImpl implements PolicyHistoryService {

    private final PolicyHistoryRepository policyHistoryRepository;
    private final PolicyRepository policyRepository;

    @Override
    public PolicyHistoryResponse saveHistory(
            Long policyId,
            String action,
            Long changedBy,
            String remarks) {

        if (policyId == null || policyId <= 0) {
            throw new IllegalArgumentException(
                    "Valid policy ID is required"
            );
        }

        if (action == null || action.isBlank()) {
            throw new IllegalArgumentException(
                    "History action is required"
            );
        }

        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Policy not found with ID: " + policyId
                ));

        PolicyHistory history = new PolicyHistory();
        history.setPolicy(policy);
        history.setAction(action.trim());
        history.setChangedBy(changedBy);
        history.setChangeDate(LocalDateTime.now());
        history.setRemarks(remarks);

        PolicyHistory savedHistory =
                policyHistoryRepository.save(history);

        return mapToResponse(savedHistory);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PolicyHistoryResponse> getHistoryByPolicyId(
            Long policyId) {

        if (policyId == null || policyId <= 0) {
            throw new IllegalArgumentException(
                    "Valid policy ID is required"
            );
        }

        if (!policyRepository.existsById(policyId)) {
            throw new ResourceNotFoundException(
                    "Policy not found with ID: " + policyId
            );
        }

        return policyHistoryRepository
                .findByPolicy_PolicyId(policyId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private PolicyHistoryResponse mapToResponse(
            PolicyHistory history) {

        return new PolicyHistoryResponse(
                history.getHistoryId(),
                history.getPolicy().getPolicyId(),
                history.getAction(),
                history.getChangedBy(),
                history.getChangeDate(),
                history.getRemarks()
        );
    }
}
