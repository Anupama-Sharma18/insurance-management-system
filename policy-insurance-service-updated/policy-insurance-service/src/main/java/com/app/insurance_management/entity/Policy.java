
package com.app.insurance_management.entity;

import com.app.insurance_management.enums.PolicyStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(
        name = "policy",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_policy_policy_number",
                        columnNames = "policy_number"
                )
        }
)
@Data
public class Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "policy_id")
    private Long policyId;

    @Column(name = "policy_number", length = 50, unique = true)
    private String policyNumber;

    // IDs owned by other microservices
    @Column(name = "quote_id", nullable = false)
    private Long quoteId;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "issue_date", nullable = false)
    private LocalDate issueDate;

    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;

    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "policy_status", nullable = false)
    private PolicyStatus policyStatus;
}
