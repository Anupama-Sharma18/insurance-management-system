package policy_servicing_service.service;
import java.util.List;
import policy_servicing_service.dto.ServiceRequestRequestDto;
import policy_servicing_service.dto.ServiceRequestResponseDto;
import policy_servicing_service.dto.ServiceRequestUpdateRequestDto;
public interface ServiceRequestService {

    ServiceRequestResponseDto createServiceRequest(
            ServiceRequestRequestDto requestDto);

    List<ServiceRequestResponseDto> getAllServiceRequests();

    ServiceRequestResponseDto getServiceRequestById(Long requestId);

    ServiceRequestResponseDto updateServiceRequest(
            Long requestId,
            ServiceRequestUpdateRequestDto requestDto);
}