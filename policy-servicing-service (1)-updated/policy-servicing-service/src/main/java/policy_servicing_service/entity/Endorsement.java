
package policy_servicing_service.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import policy_servicing_service.enums.ApprovalStatus;

@Entity
@Table(name = "policy_endorsement")
public class Endorsement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "endorsement_id")
    private Long endorsementId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "request_id", nullable = false)
    private ServiceRequest serviceRequest;

    @Column(
            name = "endorsement_number",
            unique = true,
            nullable = false,
            length = 30
    )
    private String endorsementNumber;

    @Column(name = "endorsement_type", length = 100)
    private String endorsementType;

    @Column(name = "previous_value", columnDefinition = "TEXT")
    private String previousValue;

    @Column(name = "updated_value", columnDefinition = "TEXT", nullable = false)
    private String updatedValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status", nullable = false)
    private ApprovalStatus approvalStatus;

    // Maps to the mandatory created_at column in MySQL.
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Endorsement() {
    }

    public Long getEndorsementId() {
        return endorsementId;
    }

    public void setEndorsementId(Long endorsementId) {
        this.endorsementId = endorsementId;
    }

    public ServiceRequest getServiceRequest() {
        return serviceRequest;
    }

    public void setServiceRequest(ServiceRequest serviceRequest) {
        this.serviceRequest = serviceRequest;
    }

    public Long getRequestId() {
        return serviceRequest != null
                ? serviceRequest.getRequestId()
                : null;
    }

    public String getEndorsementNumber() {
        return endorsementNumber;
    }

    public void setEndorsementNumber(String endorsementNumber) {
        this.endorsementNumber = endorsementNumber;
    }

    public String getEndorsementType() {
        return endorsementType;
    }

    public void setEndorsementType(String endorsementType) {
        this.endorsementType = endorsementType;
    }

    public String getPreviousValue() {
        return previousValue;
    }

    public void setPreviousValue(String previousValue) {
        this.previousValue = previousValue;
    }

    public String getUpdatedValue() {
        return updatedValue;
    }

    public void setUpdatedValue(String updatedValue) {
        this.updatedValue = updatedValue;
    }

    public ApprovalStatus getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(ApprovalStatus approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
