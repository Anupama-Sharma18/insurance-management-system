package policy_servicing_service.service;

/**
 * Replace this test implementation with the actual Claims microservice API.
 * The supplied BRD defines claim eligibility rules but does not specify an
 * active-claims endpoint, so this interface keeps that integration explicit.
 */
public interface ClaimsEligibilityService {
    boolean hasActiveClaims(Long policyId);
}
