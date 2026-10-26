
package policy_servicing_service.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import policy_servicing_service.dto.EndorsementRequestDto;
import policy_servicing_service.dto.EndorsementResponseDto;
import policy_servicing_service.entity.Endorsement;
import policy_servicing_service.entity.ServiceRequest;
import policy_servicing_service.enums.ApprovalStatus;
import policy_servicing_service.exception.ResourceNotFoundException;
import policy_servicing_service.repository.EndorsementRepository;
import policy_servicing_service.repository.ServiceRequestRepository;
import policy_servicing_service.service.EndorsementService;

@Service
@RequiredArgsConstructor
@Slf4j
public class EndorsementServiceImpl implements EndorsementService {

    private final EndorsementRepository endorsementRepository;
    private final ServiceRequestRepository serviceRequestRepository;

    @Override
    @Transactional
    public EndorsementResponseDto createEndorsement(
            EndorsementRequestDto requestDto) {

        log.info("Creating endorsement for service request ID={}",
                requestDto.getRequestId());

        ServiceRequest serviceRequest =
                serviceRequestRepository.findById(requestDto.getRequestId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Service request not found: "
                                                + requestDto.getRequestId()));

        Endorsement endorsement = new Endorsement();

        endorsement.setServiceRequest(serviceRequest);

        endorsement.setEndorsementNumber(
                "END-" + UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase());

        endorsement.setEndorsementType(
                requestDto.getEndorsementType());

        endorsement.setPreviousValue(
                requestDto.getPreviousValue());

        endorsement.setUpdatedValue(
                requestDto.getUpdatedValue());

        endorsement.setApprovalStatus(ApprovalStatus.PENDING);

        // Fix: MySQL requires created_at to have a value.
        endorsement.setCreatedAt(LocalDateTime.now());

        Endorsement saved = endorsementRepository.save(endorsement);

        log.info("Endorsement created successfully. endorsementId={}, requestId={}",
                saved.getEndorsementId(),
                requestDto.getRequestId());

        return convertToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EndorsementResponseDto> getAllEndorsements() {

        log.info("Fetching all endorsements");

        return endorsementRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EndorsementResponseDto getEndorsementById(
            Long endorsementId) {

        return convertToResponse(getEndorsement(endorsementId));
    }

    @Override
    @Transactional
    public EndorsementResponseDto approveEndorsement(
            Long endorsementId) {

        Endorsement endorsement = getEndorsement(endorsementId);

        if (endorsement.getApprovalStatus() != ApprovalStatus.PENDING) {
            throw new IllegalStateException(
                    "Only pending endorsements can be approved");
        }

        endorsement.setApprovalStatus(ApprovalStatus.APPROVED);

        Endorsement saved = endorsementRepository.save(endorsement);

        log.info("Endorsement approved. endorsementId={}", endorsementId);

        return convertToResponse(saved);
    }

    @Override
    @Transactional
    public EndorsementResponseDto rejectEndorsement(
            Long endorsementId) {

        Endorsement endorsement = getEndorsement(endorsementId);

        if (endorsement.getApprovalStatus() != ApprovalStatus.PENDING) {
            throw new IllegalStateException(
                    "Only pending endorsements can be rejected");
        }

        endorsement.setApprovalStatus(ApprovalStatus.REJECTED);

        Endorsement saved = endorsementRepository.save(endorsement);

        log.info("Endorsement rejected. endorsementId={}", endorsementId);

        return convertToResponse(saved);
    }

    private Endorsement getEndorsement(Long endorsementId) {

        return endorsementRepository.findById(endorsementId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Endorsement not found: " + endorsementId));
    }

    private EndorsementResponseDto convertToResponse(
            Endorsement endorsement) {

        return EndorsementResponseDto.builder()
                .endorsementId(endorsement.getEndorsementId())
                .requestId(endorsement.getServiceRequest().getRequestId())
                .endorsementNumber(endorsement.getEndorsementNumber())
                .endorsementType(endorsement.getEndorsementType())
                .previousValue(endorsement.getPreviousValue())
                .updatedValue(endorsement.getUpdatedValue())
                .approvalStatus(endorsement.getApprovalStatus())
                .build();
    }
}
