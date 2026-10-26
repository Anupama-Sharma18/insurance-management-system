
package com.app.insurance_management.service;

import com.app.insurance_management.entity.PolicyCoverage;

import java.util.List;

public interface PolicyCoverageService {

    PolicyCoverage addCoverage(
            Long policyId,
            PolicyCoverage coverage
    );

    List<PolicyCoverage> getCoveragesByPolicyId(Long policyId);

    PolicyCoverage getCoverageById(Long coverageId);

    PolicyCoverage updateCoverage(
            Long coverageId,
            PolicyCoverage coverage
    );

    void deleteCoverage(Long coverageId);
}
