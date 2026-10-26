
package policy_servicing_service.controller;

import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import policy_servicing_service.dto.NomineeDetailsRequestDto;
import policy_servicing_service.dto.NomineeDetailsResponseDto;
import policy_servicing_service.service.NomineeDetailsService;

@RestController
@RequestMapping("/api/v1/nominees")
@RequiredArgsConstructor
public class NomineeDetailsController {

    private final NomineeDetailsService nomineeService;

    @PostMapping
    public ResponseEntity<NomineeDetailsResponseDto> addNominee(
            @Valid @RequestBody NomineeDetailsRequestDto requestDto) {

        NomineeDetailsResponseDto response =
                nomineeService.addNominee(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/policy/{policyId}")
    public ResponseEntity<List<NomineeDetailsResponseDto>> getNomineesByPolicyId(
            @PathVariable Long policyId) {

        return ResponseEntity.ok(
                nomineeService.getNomineesByPolicyId(policyId));
    }

    @GetMapping("/{nomineeId}")
    public ResponseEntity<NomineeDetailsResponseDto> getNomineeById(
            @PathVariable Long nomineeId) {

        return ResponseEntity.ok(
                nomineeService.getNomineeById(nomineeId));
    }

    @PutMapping("/{nomineeId}")
    public ResponseEntity<NomineeDetailsResponseDto> updateNominee(
            @PathVariable Long nomineeId,
            @Valid @RequestBody NomineeDetailsRequestDto requestDto) {

        return ResponseEntity.ok(
                nomineeService.updateNominee(nomineeId, requestDto));
    }

    @DeleteMapping("/{nomineeId}")
    public ResponseEntity<Void> deleteNominee(
            @PathVariable Long nomineeId) {

        nomineeService.deleteNominee(nomineeId);

        return ResponseEntity.noContent().build();
    }
}
