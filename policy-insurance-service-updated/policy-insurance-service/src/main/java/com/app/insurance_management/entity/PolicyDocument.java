
package com.app.insurance_management.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "policy_document")
@Data
public class PolicyDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    private Long documentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    @Column(name = "document_name", length = 200)
    private String documentName;

    @Column(name = "file_path", length = 500)
    private String filePath;

    @Column(name = "version_no")
    private Integer versionNo;

    @Column(name = "generated_date")
    private LocalDateTime generatedDate;
}
