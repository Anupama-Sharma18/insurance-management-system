
        package com.app.insurance_management.dto;

import java.time.LocalDateTime;

public record PolicyHistoryResponse(
        Long historyId,
        Long policyId,
        String action,
        Long changedBy,
        LocalDateTime changeDate,
        String remarks
) {
}
