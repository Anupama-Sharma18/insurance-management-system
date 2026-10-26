
        package com.app.insurance_management.service;

import com.app.insurance_management.dto.PolicyHistoryResponse;

import java.util.List;

public interface PolicyHistoryService {

    PolicyHistoryResponse saveHistory(
            Long policyId,
            String action,
            Long changedBy,
            String remarks
    );

    List<PolicyHistoryResponse> getHistoryByPolicyId(Long policyId);
}
