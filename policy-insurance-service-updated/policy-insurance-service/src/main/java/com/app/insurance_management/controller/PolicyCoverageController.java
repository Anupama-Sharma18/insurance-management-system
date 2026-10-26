
package com.app.insurance_management.controller;

import com.app.insurance_management.apiResponse.ApiResponse;
import com.app.insurance_management.entity.PolicyCoverage;
import com.app.insurance_management.service.PolicyCoverageService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/policies")
@RequiredArgsConstructor
public class PolicyCoverageController {

    private final PolicyCoverageService policyCoverageService;

    @PostMapping("/{policyId}/coverages")
    public ResponseEntity<ApiResponse<PolicyCoverage>> addCoverage(
            @PathVariable Long policyId,
            @RequestBody PolicyCoverage coverage) {

        PolicyCoverage savedCoverage =
                policyCoverageService.addCoverage(policyId, coverage);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Coverage added successfully",
                        savedCoverage
                ));
    }

    @GetMapping("/{policyId}/coverages")
    public ResponseEntity<ApiResponse<List<PolicyCoverage>>> getCoverages(
            @PathVariable Long policyId) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Coverages fetched successfully",
                        policyCoverageService
                                .getCoveragesByPolicyId(policyId)
                )
        );
    }

    @GetMapping("/coverages/{coverageId}")
    public ResponseEntity<ApiResponse<PolicyCoverage>> getCoverageById(
            @PathVariable Long coverageId) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Coverage fetched successfully",
                        policyCoverageService.getCoverageById(coverageId)
                )
        );
    }

    @PutMapping("/coverages/{coverageId}")
    public ResponseEntity<ApiResponse<PolicyCoverage>> updateCoverage(
            @PathVariable Long coverageId,
            @RequestBody PolicyCoverage coverage) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Coverage updated successfully",
                        policyCoverageService.updateCoverage(
                                coverageId, coverage
                        )
                )
        );
    }

    @DeleteMapping("/coverages/{coverageId}")
    public ResponseEntity<ApiResponse<Void>> deleteCoverage(
            @PathVariable Long coverageId) {

        policyCoverageService.deleteCoverage(coverageId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Coverage deleted successfully",
                        null
                )
        );
    }
}
