
package com.app.insurance_management.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PolicyDocumentResponse {

    private Long documentId;
    private Long policyId;
    private String policyNumber;
    private String documentName;
    private String filePath;
    private Integer versionNo;
    private LocalDateTime generatedDate;
}
