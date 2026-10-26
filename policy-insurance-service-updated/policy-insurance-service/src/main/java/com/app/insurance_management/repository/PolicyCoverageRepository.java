
package com.app.insurance_management.repository;

import com.app.insurance_management.entity.PolicyCoverage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PolicyCoverageRepository
        extends JpaRepository<PolicyCoverage, Long> {

    List<PolicyCoverage> findByPolicy_PolicyId(Long policyId);
}
