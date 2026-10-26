
package policy_servicing_service.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "service_request_history")
public class ServiceRequestHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long historyId;

    @Column(name = "request_id")
    private Long requestId;

    @Column(name = "action", length = 100)
    private String action;

    @Column(name = "action_by")
    private Long actionBy;

    @Column(name = "action_date")
    private LocalDateTime actionDate;

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

    public ServiceRequestHistory() {
    }

    public Long getHistoryId() {
        return historyId;
    }

    public void setHistoryId(Long historyId) {
        this.historyId = historyId;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Long getActionBy() {
        return actionBy;
    }

    public void setActionBy(Long actionBy) {
        this.actionBy = actionBy;
    }

    public LocalDateTime getActionDate() {
        return actionDate;
    }

    public void setActionDate(LocalDateTime actionDate) {
        this.actionDate = actionDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
