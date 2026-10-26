
        package policy_servicing_service.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import policy_servicing_service.dto.PolicyDetailsDto;
import policy_servicing_service.dto.ServiceRequestRequestDto;
import policy_servicing_service.dto.ServiceRequestResponseDto;
import policy_servicing_service.dto.ServiceRequestUpdateRequestDto;
import policy_servicing_service.entity.ServiceRequest;
import policy_servicing_service.entity.ServiceRequestHistory;
import policy_servicing_service.enums.ServiceRequestStatus;
import policy_servicing_service.exception.ResourceNotFoundException;
import policy_servicing_service.repository.ServiceRequestHistoryRepository;
import policy_servicing_service.repository.ServiceRequestRepository;
import policy_servicing_service.service.PolicyDataService;
import policy_servicing_service.service.ServiceRequestService;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServiceRequestServiceImpl implements ServiceRequestService {

    private final ServiceRequestRepository serviceRequestRepository;
    private final ServiceRequestHistoryRepository historyRepository;
    private final PolicyDataService policyDataService;

    @Override
    @Transactional
    public ServiceRequestResponseDto createServiceRequest(
            ServiceRequestRequestDto requestDto) {

        log.info(
                "Creating service request for policyId={}, customerId={}",
                requestDto.getPolicyId(),
                requestDto.getCustomerId()
        );

        if (requestDto.getPolicyId() == null) {
            throw new IllegalArgumentException("Policy ID is required");
        }

        if (requestDto.getCustomerId() == null) {
            throw new IllegalArgumentException("Customer ID is required");
        }

        if (requestDto.getRequestType() == null
                || requestDto.getRequestType().isBlank()) {
            throw new IllegalArgumentException("Request type is required");
        }

        if (requestDto.getRequestDetails() == null
                || requestDto.getRequestDetails().isBlank()) {
            throw new IllegalArgumentException("Request details are required");
        }

        // Verify policy exists in the Insurance Policy service.
        PolicyDetailsDto policy =
                policyDataService.getPolicyById(requestDto.getPolicyId());

        if (policy == null
                || !requestDto.getPolicyId().equals(policy.getPolicyId())) {
            throw new ResourceNotFoundException(
                    "Policy not found: " + requestDto.getPolicyId()
            );
        }

        // Verify the customer owns the policy.
        if (policy.getCustomerId() == null
                || !requestDto.getCustomerId().equals(policy.getCustomerId())) {
            throw new IllegalArgumentException(
                    "Customer ID does not match policy"
            );
        }

        LocalDateTime now = LocalDateTime.now();

        ServiceRequest request = new ServiceRequest();

        request.setPolicyId(requestDto.getPolicyId());
        request.setCustomerId(requestDto.getCustomerId());
        request.setRequestType(requestDto.getRequestType());
        request.setRequestDetails(requestDto.getRequestDetails());

        request.setRequestNumber(
                "SR-" + UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 20)
                        .toUpperCase()
        );

        request.setRequestDate(now);
        request.setCreatedAt(now);
        request.setUpdatedAt(now);
        request.setSubmittedBy(requestDto.getCustomerId());
        request.setStatus(ServiceRequestStatus.SUBMITTED);

        ServiceRequest savedRequest =
                serviceRequestRepository.save(request);

        saveHistory(
                savedRequest.getRequestId(),
                "REQUEST_CREATED",
                requestDto.getCustomerId(),
                "Service request created successfully"
        );

        log.info(
                "Service request created successfully. requestId={}",
                savedRequest.getRequestId()
        );

        return toResponseDto(savedRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceRequestResponseDto> getAllServiceRequests() {

        log.info("Fetching all service requests");

        return serviceRequestRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceRequestResponseDto getServiceRequestById(Long requestId) {

        ServiceRequest request = findRequestById(requestId);

        return toResponseDto(request);
    }

    @Override
    @Transactional
    public ServiceRequestResponseDto updateServiceRequest(
            Long requestId,
            ServiceRequestUpdateRequestDto requestDto) {

        ServiceRequest request = findRequestById(requestId);

        if (requestDto == null || requestDto.getStatus() == null) {
            throw new IllegalArgumentException(
                    "Service request status is required"
            );
        }

        LocalDateTime now = LocalDateTime.now();

        ServiceRequestStatus previousStatus = request.getStatus();

        request.setStatus(requestDto.getStatus());
        request.setUpdatedAt(now);

        ServiceRequest updatedRequest =
                serviceRequestRepository.save(request);

        String remarks = requestDto.getRemarks();

        if (remarks == null || remarks.isBlank()) {
            remarks = "Status changed from "
                    + previousStatus
                    + " to "
                    + requestDto.getStatus();
        }

        saveHistory(
                updatedRequest.getRequestId(),
                requestDto.getStatus().name(),
                requestDto.getActionBy(),
                remarks
        );

        log.info(
                "Service request updated. requestId={}, oldStatus={}, newStatus={}",
                updatedRequest.getRequestId(),
                previousStatus,
                updatedRequest.getStatus()
        );

        return toResponseDto(updatedRequest);
    }

    private ServiceRequest findRequestById(Long requestId) {

        if (requestId == null) {
            throw new IllegalArgumentException(
                    "Service request ID is required"
            );
        }

        return serviceRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Service request not found: " + requestId
                ));
    }

    private ServiceRequestResponseDto toResponseDto(
            ServiceRequest request) {

        ServiceRequestResponseDto response =
                new ServiceRequestResponseDto();

        response.setRequestId(request.getRequestId());
        response.setRequestNumber(request.getRequestNumber());
        response.setPolicyId(request.getPolicyId());
        response.setCustomerId(request.getCustomerId());
        response.setRequestType(request.getRequestType());
        response.setRequestDate(request.getRequestDate());
        response.setStatus(request.getStatus());

        return response;
    }

    private void saveHistory(
            Long requestId,
            String action,
            Long actionBy,
            String remarks) {

        ServiceRequestHistory history = new ServiceRequestHistory();

        history.setRequestId(requestId);
        history.setAction(action);
        history.setActionBy(actionBy);
        history.setActionDate(LocalDateTime.now());
        history.setRemarks(remarks);

        historyRepository.save(history);
    }
}
