
package com.app.insurance_management.controller;

import com.app.insurance_management.apiResponse.ApiResponse;
import com.app.insurance_management.dto.PolicyRiderResponse;
import com.app.insurance_management.entity.PolicyRider;
import com.app.insurance_management.service.PolicyRiderService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/policies")
@RequiredArgsConstructor
public class PolicyRiderController {

    private final PolicyRiderService policyRiderService;

    @PostMapping("/{policyId}/riders")
    public ResponseEntity<ApiResponse<PolicyRiderResponse>> addRider(
            @PathVariable Long policyId,
            @RequestBody PolicyRider rider) {

        PolicyRiderResponse saved =
                policyRiderService.addRider(policyId, rider);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Rider added successfully",
                        saved
                ));
    }

    @GetMapping("/{policyId}/riders")
    public ResponseEntity<ApiResponse<List<PolicyRiderResponse>>>
    getRidersByPolicyId(@PathVariable Long policyId) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Riders fetched successfully",
                        policyRiderService.getRidersByPolicyId(policyId)
                )
        );
    }

    @GetMapping("/riders/{policyRiderId}")
    public ResponseEntity<ApiResponse<PolicyRiderResponse>> getRiderById(
            @PathVariable Long policyRiderId) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Rider fetched successfully",
                        policyRiderService.getRiderById(policyRiderId)
                )
        );
    }

    @PutMapping("/riders/{policyRiderId}")
    public ResponseEntity<ApiResponse<PolicyRiderResponse>> updateRider(
            @PathVariable Long policyRiderId,
            @RequestBody PolicyRider rider) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Rider updated successfully",
                        policyRiderService.updateRider(policyRiderId, rider)
                )
        );
    }

    @DeleteMapping("/riders/{policyRiderId}")
    public ResponseEntity<ApiResponse<Void>> removeRider(
            @PathVariable Long policyRiderId) {

        policyRiderService.removeRider(policyRiderId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Rider removed successfully",
                        null
                )
        );
    }
}
