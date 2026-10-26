package policy_servicing_service.service.impl;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import policy_servicing_service.service.ClaimsEligibilityService;

/**
 * TEST ONLY: assumes there are no active claims.
 * Set claims.eligibility.test-mode=false and provide a real implementation
 * before using this service outside local development.
 */
@Service
@ConditionalOnProperty(name = "claims.eligibility.test-mode", havingValue = "true", matchIfMissing = true)
public class TestClaimsEligibilityService implements ClaimsEligibilityService {
    @Override
    public boolean hasActiveClaims(Long policyId) {
        return false;
    }
}
