
package policy_servicing_service.controller;

import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import policy_servicing_service.dto.EndorsementRequestDto;
import policy_servicing_service.dto.EndorsementResponseDto;
import policy_servicing_service.service.EndorsementService;

@RestController
@RequestMapping("/api/v1/endorsements")
@RequiredArgsConstructor
public class EndorsementController {

    private final EndorsementService endorsementService;

    @PostMapping
    public ResponseEntity<EndorsementResponseDto> createEndorsement(
            @Valid @RequestBody EndorsementRequestDto requestDto) {

        EndorsementResponseDto response =
                endorsementService.createEndorsement(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<EndorsementResponseDto>> getAllEndorsements() {

        return ResponseEntity.ok(
                endorsementService.getAllEndorsements());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EndorsementResponseDto> getEndorsementById(
            @PathVariable("id") Long id) {

        return ResponseEntity.ok(
                endorsementService.getEndorsementById(id));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<EndorsementResponseDto> approveEndorsement(
            @PathVariable("id") Long id) {

        return ResponseEntity.ok(
                endorsementService.approveEndorsement(id));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<EndorsementResponseDto> rejectEndorsement(
            @PathVariable("id") Long id) {

        return ResponseEntity.ok(
                endorsementService.rejectEndorsement(id));
    }
}
