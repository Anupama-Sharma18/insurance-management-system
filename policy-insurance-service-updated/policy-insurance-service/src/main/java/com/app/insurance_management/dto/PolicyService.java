
package com.app.insurance_management.dto;

import com.app.insurance_management.dto.PolicyRequest;
import com.app.insurance_management.dto.PolicyResponse;

import java.util.List;

public interface PolicyService {

    PolicyResponse createPolicy(PolicyRequest request);

    List<PolicyResponse> getAllPolicies();

    PolicyResponse getPolicyById(Long policyId);

    PolicyResponse updatePolicy(Long policyId, PolicyRequest request);

    void cancelPolicy(Long policyId);

    PolicyResponse activatePolicy(Long policyId);

    PolicyResponse suspendPolicy(Long policyId);

    PolicyResponse expirePolicy(Long policyId);
}
