
package com.app.insurance_management.dto;

import com.app.insurance_management.enums.PolicyStatus;

import java.time.LocalDate;

public record PolicyResponse(

        Long policyId,

        String policyNumber,

        Long quoteId,

        Long customerId,

        Long productId,

        LocalDate issueDate,

        LocalDate effectiveDate,

        LocalDate expiryDate,

        PolicyStatus policyStatus

) {
}
