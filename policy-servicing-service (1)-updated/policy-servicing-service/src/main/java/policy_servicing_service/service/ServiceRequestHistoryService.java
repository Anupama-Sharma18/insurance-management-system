
package policy_servicing_service.service;

import java.util.List;

import policy_servicing_service.dto.ServiceRequestHistoryResponseDto;

public interface ServiceRequestHistoryService {

    List<ServiceRequestHistoryResponseDto> getAllHistory();

    List<ServiceRequestHistoryResponseDto> getHistoryByRequestId(Long requestId);

    ServiceRequestHistoryResponseDto getHistoryById(Long historyId);
    ServiceRequestHistoryResponseDto addHistory(
            Long requestId, String action, Long actionBy, String remarks);
}
