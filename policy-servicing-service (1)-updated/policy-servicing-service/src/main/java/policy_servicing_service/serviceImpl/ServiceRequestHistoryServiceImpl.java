
package policy_servicing_service.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import policy_servicing_service.dto.ServiceRequestHistoryResponseDto;
import policy_servicing_service.entity.ServiceRequestHistory;
import policy_servicing_service.exception.ResourceNotFoundException;
import policy_servicing_service.repository.ServiceRequestHistoryRepository;
import policy_servicing_service.service.ServiceRequestHistoryService;

@Service
@RequiredArgsConstructor
public class ServiceRequestHistoryServiceImpl
        implements ServiceRequestHistoryService {

    private final ServiceRequestHistoryRepository historyRepository;

    // Add history
    @Override
    public ServiceRequestHistoryResponseDto addHistory(
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

        ServiceRequestHistory savedHistory =
                historyRepository.save(history);

        return convertToResponse(savedHistory);
    }

    // Get all history
    @Override
    public List<ServiceRequestHistoryResponseDto> getAllHistory() {

        return historyRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Get history by service request ID
    @Override
    public List<ServiceRequestHistoryResponseDto> getHistoryByRequestId(
            Long requestId) {

        return historyRepository.findByRequestId(requestId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Get history by history ID
    @Override
    public ServiceRequestHistoryResponseDto getHistoryById(Long historyId) {

        ServiceRequestHistory history =
                historyRepository.findById(historyId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "History not found with ID: "
                                                + historyId));

        return convertToResponse(history);
    }

    // Convert entity to response DTO
    private ServiceRequestHistoryResponseDto convertToResponse(
            ServiceRequestHistory history) {

        return ServiceRequestHistoryResponseDto.builder()
                .historyId(history.getHistoryId())
                .requestId(history.getRequestId())
                .action(history.getAction())
                .actionBy(history.getActionBy())
                .actionDate(history.getActionDate())
                .remarks(history.getRemarks())
                .build();
    }
}
