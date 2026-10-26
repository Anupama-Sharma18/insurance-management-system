
package policy_servicing_service.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "policy_cancellation")
public class PolicyCancellation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cancellation_id")
    private Long cancellationId;

    @Column(name = "policy_id")
    private Long policyId;

    @Column(name = "cancellation_date")
    private LocalDate cancellationDate;

    @Column(name = "cancellation_reason", columnDefinition = "TEXT")
    private String cancellationReason;

    @Column(name = "refund_amount", precision = 15, scale = 2)
    private BigDecimal refundAmount;

    @Column(name = "approved_by")
    private Long approvedBy;

    @Column(name = "cancellation_request_number", unique = true, length = 40)
    private String cancellationRequestNumber;

    @Column(name = "request_status", length = 30, nullable = false)
    private String requestStatus = "PENDING";

    @Column(name = "decision_remarks", length = 1000)
    private String decisionRemarks;

    @Column(name = "decision_date")
    private java.time.LocalDateTime decisionDate;

    public PolicyCancellation() {
    }

    public Long getCancellationId() {
        return cancellationId;
    }

    public void setCancellationId(Long cancellationId) {
        this.cancellationId = cancellationId;
    }

    public Long getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Long policyId) {
        this.policyId = policyId;
    }

    public LocalDate getCancellationDate() {
        return cancellationDate;
    }

    public void setCancellationDate(LocalDate cancellationDate) {
        this.cancellationDate = cancellationDate;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public Long getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Long approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getCancellationRequestNumber() { return cancellationRequestNumber; }
    public void setCancellationRequestNumber(String value) { this.cancellationRequestNumber = value; }

    public String getRequestStatus() { return requestStatus; }
    public void setRequestStatus(String value) { this.requestStatus = value; }

    public String getDecisionRemarks() { return decisionRemarks; }
    public void setDecisionRemarks(String value) { this.decisionRemarks = value; }

    public java.time.LocalDateTime getDecisionDate() { return decisionDate; }
    public void setDecisionDate(java.time.LocalDateTime value) { this.decisionDate = value; }
}
