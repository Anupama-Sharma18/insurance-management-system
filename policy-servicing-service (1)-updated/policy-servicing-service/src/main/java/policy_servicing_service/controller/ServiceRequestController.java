package policy_servicing_service.controller;

import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import policy_servicing_service.dto.ServiceRequestRequestDto;
import policy_servicing_service.dto.ServiceRequestResponseDto;
import policy_servicing_service.dto.ServiceRequestUpdateRequestDto;
import policy_servicing_service.service.ServiceRequestService;

@RestController
@RequestMapping("/api/v1/service-requests")
@RequiredArgsConstructor
public class ServiceRequestController {

    private final ServiceRequestService serviceRequestService;

    @PostMapping
    public ResponseEntity<ServiceRequestResponseDto> createServiceRequest(
            @Valid @RequestBody ServiceRequestRequestDto requestDto) {

        ServiceRequestResponseDto response =
                serviceRequestService.createServiceRequest(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ServiceRequestResponseDto>> getAllServiceRequests() {

        return ResponseEntity.ok(
                serviceRequestService.getAllServiceRequests());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceRequestResponseDto> getServiceRequestById(
            @PathVariable("id") Long id) {

        return ResponseEntity.ok(
                serviceRequestService.getServiceRequestById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceRequestResponseDto> updateServiceRequest(
            @PathVariable("id") Long id,
            @Valid @RequestBody ServiceRequestUpdateRequestDto requestDto) {

        return ResponseEntity.ok(
                serviceRequestService.updateServiceRequest(id, requestDto));
    }
}
