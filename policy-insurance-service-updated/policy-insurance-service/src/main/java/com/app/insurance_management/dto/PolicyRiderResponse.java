
package com.app.insurance_management.dto;

import java.math.BigDecimal;

public record PolicyRiderResponse(
        Long policyRiderId,
        Long policyId,
        Long riderId,
        BigDecimal riderPremium
) {
}
