
package com.app.insurance_management.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "policy_coverage")
@Getter
@Setter
public class PolicyCoverage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coverage_id")
    private Long coverageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id", nullable = false)
    @JsonIgnoreProperties({
            "hibernateLazyInitializer",
            "handler"
    })
    private Policy policy;

    @Column(name = "coverage_name", length = 150)
    private String coverageName;

    @Column(name = "sum_insured", precision = 15, scale = 2)
    private BigDecimal sumInsured;

    @Column(name = "coverage_start_date")
    private LocalDate coverageStartDate;

    @Column(name = "coverage_end_date")
    private LocalDate coverageEndDate;
}
