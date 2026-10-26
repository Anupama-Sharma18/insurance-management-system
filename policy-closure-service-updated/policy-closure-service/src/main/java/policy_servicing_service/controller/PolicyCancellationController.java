package policy_servicing_service.controller;

import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import policy_servicing_service.dto.CancellationDecisionRequestDto;
import policy_servicing_service.dto.PolicyCancellationRequestDto;
import policy_servicing_service.dto.PolicyCancellationResponseDto;
import policy_servicing_service.service.PolicyCancellationService;

@RestController
@RequestMapping("/api/v1/cancellations")
@RequiredArgsConstructor
public class PolicyCancellationController {

    private final PolicyCancellationService cancellationService;

    @PostMapping("/policy/{policyId}")
    public ResponseEntity<PolicyCancellationResponseDto> cancelPolicy(
            @PathVariable Long policyId,
            @Valid @RequestBody PolicyCancellationRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cancellationService.cancelPolicy(requestDto, policyId));
    }


    @PutMapping("/{cancellationId}/approve")
    public ResponseEntity<PolicyCancellationResponseDto> approve(
            @PathVariable Long cancellationId,
            @Valid @RequestBody CancellationDecisionRequestDto request) {
        return ResponseEntity.ok(cancellationService.approve(cancellationId, request));
    }

    @PutMapping("/{cancellationId}/reject")
    public ResponseEntity<PolicyCancellationResponseDto> reject(
            @PathVariable Long cancellationId,
            @Valid @RequestBody CancellationDecisionRequestDto request) {
        return ResponseEntity.ok(cancellationService.reject(cancellationId, request));
    }

    @GetMapping
    public ResponseEntity<List<PolicyCancellationResponseDto>> getAllCancellations() {
        return ResponseEntity.ok(cancellationService.getAllCancellations());
    }

    @GetMapping("/{cancellationId}")
    public ResponseEntity<PolicyCancellationResponseDto> getCancellationById(
            @PathVariable Long cancellationId) {
        return ResponseEntity.ok(
                cancellationService.getCancellationById(cancellationId));
    }

    @GetMapping("/policy/{policyId}")
    public ResponseEntity<List<PolicyCancellationResponseDto>> getByPolicyId(
            @PathVariable Long policyId) {
        return ResponseEntity.ok(
                cancellationService.getCancellationsByPolicyId(policyId));
    }
}
