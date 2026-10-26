package policy_servicing_service.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRequestHistoryResponseDto {

    private Long historyId;

    private Long requestId;

    private String action;

    private Long actionBy;

    private LocalDateTime actionDate;

    private String remarks;
}