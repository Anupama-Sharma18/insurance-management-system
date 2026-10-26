
package com.app.insurance_management.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "policy_history")
@Data
public class PolicyHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long historyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    @Column(name = "action", length = 100, nullable = false)
    private String action;

    @Column(name = "changed_by")
    private Long changedBy;

    @Column(name = "change_date", nullable = false)
    private LocalDateTime changeDate;

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;
}
