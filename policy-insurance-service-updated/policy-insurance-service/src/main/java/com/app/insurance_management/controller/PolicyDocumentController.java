
package com.app.insurance_management.controller;

import com.app.insurance_management.apiResponse.ApiResponse;
import com.app.insurance_management.dto.PolicyDocumentResponse;
import com.app.insurance_management.entity.PolicyDocument;
import com.app.insurance_management.service.PolicyDocumentService;

import lombok.RequiredArgsConstructor;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/v1/policies")
@RequiredArgsConstructor
public class PolicyDocumentController {

    private final PolicyDocumentService policyDocumentService;

    @PostMapping("/{policyId}/document")
    public ResponseEntity<ApiResponse<PolicyDocumentResponse>> generateDocument(
            @PathVariable Long policyId) {

        PolicyDocumentResponse response =
                policyDocumentService.generateDocument(policyId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Policy document generated successfully",
                        response
                ));
    }

    // Download the latest policy PDF.
    @GetMapping("/{policyId}/document")
    public ResponseEntity<Resource> downloadPolicyDocument(
            @PathVariable Long policyId) throws IOException {

        List<PolicyDocument> documents =
                policyDocumentService.getDocumentsByPolicyId(policyId);

        if (documents.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        PolicyDocument document = documents.stream()
                .filter(d -> d.getVersionNo() != null)
                .max((a, b) -> Integer.compare(
                        a.getVersionNo(), b.getVersionNo()))
                .orElseThrow(() ->
                        new IllegalStateException(
                                "No generated policy document found"));

        return downloadPdf(document);
    }

    @GetMapping("/documents/{documentId}")
    public ResponseEntity<ApiResponse<PolicyDocumentResponse>> getDocumentById(
            @PathVariable Long documentId) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Policy document fetched successfully",
                        policyDocumentService.getDocumentById(documentId)
                )
        );
    }

    @PostMapping("/{policyId}/document/regenerate")
    public ResponseEntity<ApiResponse<PolicyDocumentResponse>> regenerateDocument(
            @PathVariable Long policyId) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Policy document regenerated successfully",
                        policyDocumentService.regenerateDocument(policyId)
                )
        );
    }

    // Download a specific document version.
    @GetMapping("/documents/{documentId}/download")
    public ResponseEntity<Resource> downloadDocument(
            @PathVariable Long documentId) throws IOException {

        PolicyDocument document =
                policyDocumentService.getDocumentEntityById(documentId);

        return downloadPdf(document);
    }

    private ResponseEntity<Resource> downloadPdf(
            PolicyDocument document) throws IOException {

        if (document.getFilePath() == null
                || document.getFilePath().isBlank()) {
            return ResponseEntity.notFound().build();
        }

        Path filePath = Path.of(document.getFilePath())
                .toAbsolutePath()
                .normalize();

        if (!Files.isRegularFile(filePath)
                || !Files.isReadable(filePath)) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new FileSystemResource(filePath);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\""
                                + filePath.getFileName() + "\"")
                .body(resource);
    }
}
