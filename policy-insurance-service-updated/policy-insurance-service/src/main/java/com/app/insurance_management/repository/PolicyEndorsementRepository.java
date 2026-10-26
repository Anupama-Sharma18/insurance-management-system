
package com.app.insurance_management.repository;

import com.app.insurance_management.entity.PolicyEndorsement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PolicyEndorsementRepository
        extends JpaRepository<PolicyEndorsement, Long> {

    List<PolicyEndorsement> findByPolicy_PolicyId(Long policyId);
}
