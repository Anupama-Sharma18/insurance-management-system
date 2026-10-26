
package policy_servicing_service.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import policy_servicing_service.dto.ServiceRequestHistoryResponseDto;
import policy_servicing_service.service.ServiceRequestHistoryService;

@RestController
@RequestMapping("/api/v1/service-request-history")
@RequiredArgsConstructor
public class ServiceRequestHistoryController {

    private final ServiceRequestHistoryService historyService;

    @GetMapping
    public ResponseEntity<List<ServiceRequestHistoryResponseDto>> getAllHistory() {

        return ResponseEntity.ok(historyService.getAllHistory());
    }

    @GetMapping("/{historyId}")
    public ResponseEntity<ServiceRequestHistoryResponseDto> getHistoryById(
            @PathVariable Long historyId) {

        return ResponseEntity.ok(
                historyService.getHistoryById(historyId));
    }

    @GetMapping("/request/{requestId}")
    public ResponseEntity<List<ServiceRequestHistoryResponseDto>> getHistoryByRequestId(
            @PathVariable Long requestId) {

        return ResponseEntity.ok(
                historyService.getHistoryByRequestId(requestId));
    }
}
