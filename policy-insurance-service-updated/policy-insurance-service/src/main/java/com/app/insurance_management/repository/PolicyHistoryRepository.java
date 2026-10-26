
package com.app.insurance_management.repository;

import com.app.insurance_management.entity.PolicyHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PolicyHistoryRepository
        extends JpaRepository<PolicyHistory, Long> {

    List<PolicyHistory> findByPolicy_PolicyId(Long policyId);
}
