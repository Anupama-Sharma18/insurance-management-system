
package com.app.insurance_management.repository;

import com.app.insurance_management.entity.PolicyDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PolicyDocumentRepository
        extends JpaRepository<PolicyDocument, Long> {

    List<PolicyDocument> findByPolicy_PolicyId(Long policyId);
}
