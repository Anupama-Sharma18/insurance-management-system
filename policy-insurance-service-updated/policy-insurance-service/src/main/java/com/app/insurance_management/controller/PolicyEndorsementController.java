
        package com.app.insurance_management.controller;

import com.app.insurance_management.apiResponse.ApiResponse;
import com.app.insurance_management.dto.PolicyEndorsementResponse;
import com.app.insurance_management.entity.PolicyEndorsement;
import com.app.insurance_management.service.PolicyEndorsementService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PolicyEndorsementController {

    private final PolicyEndorsementService endorsementService;

    @PostMapping("/policies/{policyId}/endorsements")
    public ResponseEntity<ApiResponse<PolicyEndorsementResponse>>
    createEndorsement(
            @PathVariable Long policyId,
            @RequestBody PolicyEndorsement endorsement) {

        PolicyEndorsementResponse created =
                endorsementService.createEndorsement(
                        policyId, endorsement
                );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Endorsement created successfully",
                        created
                ));
    }

    @GetMapping("/policies/{policyId}/endorsements")
    public ResponseEntity<ApiResponse<List<PolicyEndorsementResponse>>>
    getEndorsements(@PathVariable Long policyId) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Endorsements fetched successfully",
                        endorsementService
                                .getEndorsementsByPolicyId(policyId)
                )
        );
    }

    @GetMapping("/policies/endorsements/{endorsementId}")
    public ResponseEntity<ApiResponse<PolicyEndorsementResponse>>
    getEndorsementById(
            @PathVariable Long endorsementId) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Endorsement fetched successfully",
                        endorsementService
                                .getEndorsementById(endorsementId)
                )
        );
    }

    @PutMapping("/endorsements/{endorsementId}/approve")
    public ResponseEntity<ApiResponse<PolicyEndorsementResponse>>
    approveEndorsement(
            @PathVariable Long endorsementId,
            @RequestParam Long approvedBy) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Endorsement approved successfully",
                        endorsementService.approveEndorsement(
                                endorsementId, approvedBy
                        )
                )
        );
    }

    @PutMapping("/endorsements/{endorsementId}/reject")
    public ResponseEntity<ApiResponse<PolicyEndorsementResponse>>
    rejectEndorsement(
            @PathVariable Long endorsementId,
            @RequestParam Long rejectedBy,
            @RequestParam String reason) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Endorsement rejected successfully",
                        endorsementService.rejectEndorsement(
                                endorsementId, rejectedBy, reason
                        )
                )
        );
    }
}
