
        package com.app.insurance_management.controller;

import com.app.insurance_management.apiResponse.ApiResponse;
import com.app.insurance_management.dto.PolicyHistoryResponse;
import com.app.insurance_management.service.PolicyHistoryService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/policies")
@RequiredArgsConstructor
public class PolicyHistoryController {

    private final PolicyHistoryService policyHistoryService;

    @GetMapping("/{policyId}/history")
    public ResponseEntity<ApiResponse<List<PolicyHistoryResponse>>>
    getHistoryByPolicyId(@PathVariable Long policyId) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Policy history fetched successfully",
                        policyHistoryService.getHistoryByPolicyId(policyId)
                )
        );
    }
}
