
package policy_servicing_service.service;

import policy_servicing_service.dto.NomineeDetailsRequestDto;
import policy_servicing_service.dto.NomineeDetailsResponseDto;

import java.util.List;

public interface NomineeDetailsService {

    NomineeDetailsResponseDto addNominee(
            NomineeDetailsRequestDto requestDto);

    List<NomineeDetailsResponseDto> getNomineesByPolicyId(
            Long policyId);

    NomineeDetailsResponseDto getNomineeById(Long nomineeId);

    NomineeDetailsResponseDto updateNominee(
            Long nomineeId,
            NomineeDetailsRequestDto requestDto);

    void deleteNominee(Long nomineeId);
}
