
        package com.app.insurance_management.dto;

import java.time.LocalDateTime;

public record PolicyEndorsementResponse(
        Long endorsementId,
        Long policyId,
        String endorsementType,
        String oldValue,
        String newValue,
        String approvalStatus,
        Long approvedBy,
        String rejectionReason,
        LocalDateTime endorsementDate
) {
}
