package policy_servicing_service.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import policy_servicing_service.enums.ServiceRequestStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRequestResponseDto {

    private Long requestId;

    private String requestNumber;

    private Long policyId;

    private Long customerId;

    private String requestType;

    private LocalDateTime requestDate;

    private ServiceRequestStatus status;
}