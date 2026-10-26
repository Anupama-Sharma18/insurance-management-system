
        package com.app.insurance_management.service;

import com.app.insurance_management.dto.PolicyEndorsementResponse;
import com.app.insurance_management.entity.PolicyEndorsement;

import java.util.List;

public interface PolicyEndorsementService {

    PolicyEndorsementResponse createEndorsement(
            Long policyId,
            PolicyEndorsement endorsement
    );

    List<PolicyEndorsementResponse> getEndorsementsByPolicyId(
            Long policyId
    );

    PolicyEndorsementResponse getEndorsementById(Long endorsementId);

    PolicyEndorsementResponse approveEndorsement(
            Long endorsementId,
            Long approvedBy
    );

    PolicyEndorsementResponse rejectEndorsement(
            Long endorsementId,
            Long rejectedBy,
            String reason
    );
}
