
package com.app.insurance_management.repository;

import com.app.insurance_management.entity.PolicyRider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PolicyRiderRepository
        extends JpaRepository<PolicyRider, Long> {

    List<PolicyRider> findByPolicy_PolicyId(Long policyId);

    List<PolicyRider> findByRiderId(Long riderId);
}
