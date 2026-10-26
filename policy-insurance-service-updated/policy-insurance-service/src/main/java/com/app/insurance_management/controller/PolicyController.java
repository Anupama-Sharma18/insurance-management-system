
package com.app.insurance_management.controller;

import com.app.insurance_management.apiResponse.ApiResponse;
import com.app.insurance_management.dto.PolicyRequest;
import com.app.insurance_management.dto.PolicyResponse;
import com.app.insurance_management.service.PolicyService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/policies")
@RequiredArgsConstructor
public class PolicyController {

    private final PolicyService policyService;

    @PostMapping
    public ResponseEntity<ApiResponse<PolicyResponse>> createPolicy(
            @Valid @RequestBody PolicyRequest request) {

        PolicyResponse response = policyService.createPolicy(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Policy created successfully",
                        response
                ));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PolicyResponse>>> getAllPolicies() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Policies fetched successfully",
                        policyService.getAllPolicies()
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PolicyResponse>> getPolicyById(
            @PathVariable("id") Long policyId) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Policy fetched successfully",
                        policyService.getPolicyById(policyId)
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PolicyResponse>> updatePolicy(
            @PathVariable("id") Long policyId,
            @Valid @RequestBody PolicyRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Policy updated successfully",
                        policyService.updatePolicy(policyId, request)
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> cancelPolicy(
            @PathVariable("id") Long policyId) {

        policyService.cancelPolicy(policyId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Policy cancelled successfully",
                        null
                )
        );
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<PolicyResponse>> activatePolicy(
            @PathVariable("id") Long policyId) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Policy activated successfully",
                        policyService.activatePolicy(policyId)
                )
        );
    }

    @PutMapping("/{id}/suspend")
    public ResponseEntity<ApiResponse<PolicyResponse>> suspendPolicy(
            @PathVariable("id") Long policyId) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Policy suspended successfully",
                        policyService.suspendPolicy(policyId)
                )
        );
    }

    @PutMapping("/{id}/expire")
    public ResponseEntity<ApiResponse<PolicyResponse>> expirePolicy(
            @PathVariable("id") Long policyId) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Policy expired successfully",
                        policyService.expirePolicy(policyId)
                )
        );
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<Void>> cancelPolicyByPut(
            @PathVariable("id") Long policyId) {

        policyService.cancelPolicy(policyId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Policy cancelled successfully",
                        null
                )
        );
    }

    // New endpoint for the Policy Renewal microservice.
    @PutMapping("/{id}/renewal-expiry")
    public ResponseEntity<ApiResponse<PolicyResponse>> renewPolicyExpiry(
            @PathVariable("id") Long policyId,
            @RequestParam("newExpiryDate") LocalDate newExpiryDate) {

        PolicyResponse response =
                policyService.renewPolicyExpiry(policyId, newExpiryDate);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Policy expiry updated successfully",
                        response
                )
        );
    }
}
