
package policy_servicing_service.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import policy_servicing_service.enums.ServiceRequestStatus;

@Entity
@Table(name = "service_request")
@Getter
@Setter
@NoArgsConstructor
public class ServiceRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long requestId;

    @Column(name = "request_number", unique = true, length = 30)
    private String requestNumber;

    @Column(name = "policy_id", nullable = false)
    private Long policyId;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "request_type", length = 100)
    private String requestType;

    @Column(name = "request_details", nullable = false)
    private String requestDetails;

    @Column(name = "request_date")
    private LocalDateTime requestDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30)
    private ServiceRequestStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "submitted_by", nullable = false)
    private Long submittedBy;
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
