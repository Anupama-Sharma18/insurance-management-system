
package policy_servicing_service.service;

import policy_servicing_service.dto.EndorsementRequestDto;
import policy_servicing_service.dto.EndorsementResponseDto;

import java.util.List;

public interface EndorsementService {

    EndorsementResponseDto createEndorsement(
            EndorsementRequestDto requestDto);

    List<EndorsementResponseDto> getAllEndorsements();

    EndorsementResponseDto getEndorsementById(Long endorsementId);

    EndorsementResponseDto approveEndorsement(Long endorsementId);

    EndorsementResponseDto rejectEndorsement(Long endorsementId);
}
