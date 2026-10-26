
package com.app.insurance_management.service;

import com.app.insurance_management.dto.PolicyDocumentResponse;
import com.app.insurance_management.entity.PolicyDocument;

import java.util.List;

public interface PolicyDocumentService {

    PolicyDocumentResponse generateDocument(Long policyId);

    List<PolicyDocument> getDocumentsByPolicyId(Long policyId);

    PolicyDocumentResponse getDocumentById(Long documentId);

    PolicyDocument getDocumentEntityById(Long documentId);

    PolicyDocumentResponse regenerateDocument(Long policyId);
}
