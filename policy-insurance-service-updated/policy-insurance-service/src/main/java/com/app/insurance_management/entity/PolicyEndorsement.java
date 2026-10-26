
package com.app.insurance_management.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "policy_endorsement")
@Data
public class PolicyEndorsement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "endorsement_id")
    private Long endorsementId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    @Column(name = "endorsement_type", length = 100, nullable = false)
    private String endorsementType;

    @Column(name = "old_value", columnDefinition = "TEXT")
    private String oldValue;

    @Column(name = "new_value", columnDefinition = "TEXT")
    private String newValue;

    @Column(name = "approval_status", length = 30, nullable = false)
    private String approvalStatus = "PENDING";

    @Column(name = "approved_by")
    private Long approvedBy;

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    @Column(name = "endorsement_date", nullable = false)
    private LocalDateTime endorsementDate;
}
