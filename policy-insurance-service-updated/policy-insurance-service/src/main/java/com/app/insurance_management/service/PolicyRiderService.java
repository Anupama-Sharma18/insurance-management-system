
package com.app.insurance_management.service;

import com.app.insurance_management.dto.PolicyRiderResponse;
import com.app.insurance_management.entity.PolicyRider;

import java.util.List;

public interface PolicyRiderService {

    PolicyRiderResponse addRider(Long policyId, PolicyRider rider);

    List<PolicyRiderResponse> getRidersByPolicyId(Long policyId);

    PolicyRiderResponse getRiderById(Long policyRiderId);

    PolicyRiderResponse updateRider(
            Long policyRiderId,
            PolicyRider rider
    );

    void removeRider(Long policyRiderId);
}
