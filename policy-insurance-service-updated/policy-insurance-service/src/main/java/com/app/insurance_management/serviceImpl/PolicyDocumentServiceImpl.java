
package com.app.insurance_management.serviceImpl;

import com.app.insurance_management.dto.PolicyDocumentResponse;
import com.app.insurance_management.entity.Policy;
import com.app.insurance_management.entity.PolicyDocument;
import com.app.insurance_management.exception.ResourceNotFoundException;
import com.app.insurance_management.repository.PolicyDocumentRepository;
import com.app.insurance_management.repository.PolicyRepository;
import com.app.insurance_management.service.PolicyDocumentService;

import lombok.RequiredArgsConstructor;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PolicyDocumentServiceImpl implements PolicyDocumentService {

    private final PolicyDocumentRepository policyDocumentRepository;
    private final PolicyRepository policyRepository;

    @Value("${policy.documents.directory:generated-policies}")
    private String documentsDirectory;

    @Override
    public PolicyDocumentResponse generateDocument(Long policyId) {

        Policy policy = findPolicy(policyId);
        int version = nextVersion(policyId);

        PolicyDocument document = createPdfDocument(policy, version);

        return toResponse(document);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PolicyDocument> getDocumentsByPolicyId(Long policyId) {

        findPolicy(policyId);

        return policyDocumentRepository
                .findByPolicy_PolicyId(policyId);
    }

    @Override
    @Transactional(readOnly = true)
    public PolicyDocumentResponse getDocumentById(Long documentId) {

        PolicyDocument document = findDocument(documentId);

        return toResponse(document);
    }

    @Override
    @Transactional(readOnly = true)
    public PolicyDocument getDocumentEntityById(Long documentId) {

        return findDocument(documentId);
    }

    @Override
    public PolicyDocumentResponse regenerateDocument(Long policyId) {

        Policy policy = findPolicy(policyId);

        PolicyDocument document =
                createPdfDocument(policy, nextVersion(policyId));

        return toResponse(document);
    }

    private PolicyDocumentResponse toResponse(PolicyDocument document) {

        PolicyDocumentResponse response = new PolicyDocumentResponse();

        response.setDocumentId(document.getDocumentId());
        response.setDocumentName(document.getDocumentName());
        response.setFilePath(document.getFilePath());
        response.setVersionNo(document.getVersionNo());
        response.setGeneratedDate(document.getGeneratedDate());

        Policy policy = document.getPolicy();

        if (policy != null) {
            response.setPolicyId(policy.getPolicyId());
            response.setPolicyNumber(policy.getPolicyNumber());
        }

        return response;
    }

    private PolicyDocument createPdfDocument(
            Policy policy,
            int version) {

        String fileName = "Policy-"
                + policy.getPolicyNumber()
                + "-v" + version + ".pdf";

        Path directory = Path.of(documentsDirectory)
                .toAbsolutePath()
                .normalize();

        Path filePath = directory.resolve(fileName).normalize();

        if (!filePath.startsWith(directory)) {
            throw new IllegalArgumentException(
                    "Invalid policy document path"
            );
        }

        try {
            Files.createDirectories(directory);

            try (PDDocument pdf = new PDDocument()) {

                PDPage page = new PDPage();
                pdf.addPage(page);

                try (PDPageContentStream content =
                             new PDPageContentStream(pdf, page)) {

                    content.beginText();
                    content.setFont(
                            new PDType1Font(
                                    Standard14Fonts.FontName.HELVETICA_BOLD),
                            18
                    );
                    content.newLineAtOffset(50, 750);
                    content.showText("INSURANCE POLICY DOCUMENT");
                    content.endText();

                    content.beginText();
                    content.setFont(
                            new PDType1Font(
                                    Standard14Fonts.FontName.HELVETICA),
                            12
                    );
                    content.newLineAtOffset(50, 700);

                    content.showText(
                            "Policy Number: " + safe(policy.getPolicyNumber())
                    );
                    content.newLineAtOffset(0, -25);

                    content.showText(
                            "Policy ID: " + policy.getPolicyId()
                    );
                    content.newLineAtOffset(0, -25);

                    content.showText(
                            "Customer ID: " + policy.getCustomerId()
                    );
                    content.newLineAtOffset(0, -25);

                    content.showText(
                            "Product ID: " + policy.getProductId()
                    );
                    content.newLineAtOffset(0, -25);

                    content.showText(
                            "Issue Date: " + policy.getIssueDate()
                    );
                    content.newLineAtOffset(0, -25);

                    content.showText(
                            "Effective Date: " + policy.getEffectiveDate()
                    );
                    content.newLineAtOffset(0, -25);

                    content.showText(
                            "Expiry Date: " + policy.getExpiryDate()
                    );
                    content.newLineAtOffset(0, -25);

                    content.showText(
                            "Status: " + policy.getPolicyStatus()
                    );

                    content.endText();
                }

                pdf.save(filePath.toFile());
            }

            PolicyDocument document = new PolicyDocument();
            document.setPolicy(policy);
            document.setDocumentName(fileName);
            document.setFilePath(filePath.toString());
            document.setVersionNo(version);
            document.setGeneratedDate(LocalDateTime.now());

            return policyDocumentRepository.save(document);

        } catch (IOException ex) {
            throw new IllegalStateException(
                    "Failed to generate policy PDF",
                    ex
            );
        }
    }

    private PolicyDocument findDocument(Long documentId) {

        return policyDocumentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Policy document not found with ID: " + documentId
                ));
    }

    private int nextVersion(Long policyId) {

        return policyDocumentRepository
                .findByPolicy_PolicyId(policyId)
                .stream()
                .map(PolicyDocument::getVersionNo)
                .filter(version -> version != null)
                .max(Integer::compareTo)
                .orElse(0) + 1;
    }

    private Policy findPolicy(Long policyId) {

        return policyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Policy not found with ID: " + policyId
                ));
    }

    private String safe(String value) {
        return value == null ? "Not assigned" : value;
    }
}
