
package com.app.insurance_management.repository;

import com.app.insurance_management.entity.Policy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PolicyRepository extends JpaRepository<Policy, Long> {

    Optional<Policy> findByPolicyNumber(String policyNumber);

    boolean existsByPolicyNumber(String policyNumber);
}
