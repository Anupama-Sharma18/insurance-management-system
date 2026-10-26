
package com.app.insurance_management.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "policy_rider")
@Data
public class PolicyRider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "policy_rider_id")
    private Long policyRiderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    // Reference to Rider Master in another module
    @Column(name = "rider_id", nullable = false)
    private Long riderId;

    @Column(name = "rider_premium", precision = 15, scale = 2)
    private BigDecimal riderPremium;
}
