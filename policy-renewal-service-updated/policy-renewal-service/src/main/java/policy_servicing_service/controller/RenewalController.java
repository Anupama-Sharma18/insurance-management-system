package policy_servicing_service.controller;

import java.util.List;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import policy_servicing_service.dto.PaymentVerificationDto;
import policy_servicing_service.dto.PolicyRenewalRequestDto;
import policy_servicing_service.dto.PolicyRenewalResponseDto;
import policy_servicing_service.service.RenewalService;

@RestController
@RequestMapping("/api/v1/renewals")
@RequiredArgsConstructor
public class RenewalController {
    private final RenewalService renewalService;

    @PostMapping
    public ResponseEntity<PolicyRenewalResponseDto> createRenewal(
            @Valid @RequestBody PolicyRenewalRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(renewalService.createRenewal(requestDto));
    }

    @GetMapping
    public ResponseEntity<List<PolicyRenewalResponseDto>> getAllRenewals() {
        return ResponseEntity.ok(renewalService.getAllRenewals());
    }

    @GetMapping("/{renewalId}")
    public ResponseEntity<PolicyRenewalResponseDto> getRenewalById(
            @PathVariable Long renewalId) {
        return ResponseEntity.ok(renewalService.getRenewalById(renewalId));
    }

    @GetMapping("/policy/{policyId}")
    public ResponseEntity<List<PolicyRenewalResponseDto>> getRenewalsByPolicyId(
            @PathVariable Long policyId) {
        return ResponseEntity.ok(renewalService.getRenewalsByPolicyId(policyId));
    }

    @PostMapping("/{renewalId}/reminder")
    public ResponseEntity<PolicyRenewalResponseDto> sendReminder(@PathVariable Long renewalId) {
        return ResponseEntity.ok(renewalService.sendReminder(renewalId));
    }

    @PostMapping("/{renewalId}/confirm")
    public ResponseEntity<PolicyRenewalResponseDto> confirmCustomer(@PathVariable Long renewalId) {
        return ResponseEntity.ok(renewalService.confirmCustomer(renewalId));
    }

    @PostMapping("/{renewalId}/verify-payment")
    public ResponseEntity<PolicyRenewalResponseDto> verifyPayment(
            @PathVariable Long renewalId,
            @Valid @RequestBody PaymentVerificationDto dto) {
        return ResponseEntity.ok(renewalService.verifyPayment(
                renewalId, Boolean.TRUE.equals(dto.getPaymentSuccessful())));
    }
}
