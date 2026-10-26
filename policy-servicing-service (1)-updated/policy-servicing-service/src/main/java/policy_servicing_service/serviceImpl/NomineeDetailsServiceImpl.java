
package policy_servicing_service.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import policy_servicing_service.dto.NomineeDetailsRequestDto;
import policy_servicing_service.dto.NomineeDetailsResponseDto;
import policy_servicing_service.entity.NomineeDetails;
import policy_servicing_service.exception.ResourceNotFoundException;
import policy_servicing_service.repository.NomineeDetailsRepository;
import policy_servicing_service.service.NomineeDetailsService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NomineeDetailsServiceImpl implements NomineeDetailsService {

    private final NomineeDetailsRepository nomineeRepository;

    @Override
    public NomineeDetailsResponseDto addNominee(
            NomineeDetailsRequestDto dto) {

        validateNominee(dto);

        List<NomineeDetails> nominees =
                nomineeRepository.findByPolicyId(dto.getPolicyId());

        BigDecimal total = dto.getSharePercentage();

        for (NomineeDetails nominee : nominees) {
            total = total.add(nominee.getSharePercentage());
        }

        if (total.compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException(
                    "Total nominee share cannot exceed 100%");
        }

        NomineeDetails nominee = new NomineeDetails();
        nominee.setPolicyId(dto.getPolicyId());
        nominee.setNomineeName(dto.getNomineeName());
        nominee.setRelationship(dto.getRelationship());
        nominee.setDob(dto.getDob());
        nominee.setSharePercentage(dto.getSharePercentage());
        nominee.setGuardianName(dto.getGuardianName());

        return convertToResponse(nomineeRepository.save(nominee));
    }

    @Override
    public List<NomineeDetailsResponseDto> getNomineesByPolicyId(
            Long policyId) {

        return nomineeRepository.findByPolicyId(policyId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    public NomineeDetailsResponseDto getNomineeById(Long nomineeId) {

        return convertToResponse(getNominee(nomineeId));
    }

    @Override
    public NomineeDetailsResponseDto updateNominee(
            Long nomineeId, NomineeDetailsRequestDto dto) {

        NomineeDetails nominee = getNominee(nomineeId);

        if (!nominee.getPolicyId().equals(dto.getPolicyId())) {
            throw new IllegalArgumentException(
                    "Policy ID cannot be changed");
        }

        validateNominee(dto);

        List<NomineeDetails> nominees =
                nomineeRepository.findByPolicyId(dto.getPolicyId());

        BigDecimal total = dto.getSharePercentage();

        for (NomineeDetails item : nominees) {
            if (!item.getNomineeId().equals(nomineeId)) {
                total = total.add(item.getSharePercentage());
            }
        }

        if (total.compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException(
                    "Total nominee share cannot exceed 100%");
        }

        nominee.setNomineeName(dto.getNomineeName());
        nominee.setRelationship(dto.getRelationship());
        nominee.setDob(dto.getDob());
        nominee.setSharePercentage(dto.getSharePercentage());
        nominee.setGuardianName(dto.getGuardianName());

        return convertToResponse(nomineeRepository.save(nominee));
    }

    @Override
    public void deleteNominee(Long nomineeId) {
        nomineeRepository.delete(getNominee(nomineeId));
    }

    private NomineeDetails getNominee(Long nomineeId) {
        return nomineeRepository.findById(nomineeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Nominee not found: " + nomineeId));
    }

    private void validateNominee(NomineeDetailsRequestDto dto) {

        LocalDate dob = dto.getDob();

        if (dob == null || !dob.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Valid date of birth is required");
        }

        if (dob.isAfter(LocalDate.now().minusYears(18))
                && (dto.getGuardianName() == null
                || dto.getGuardianName().isBlank())) {
            throw new IllegalArgumentException(
                    "Guardian name is required for a minor nominee");
        }
    }

    private NomineeDetailsResponseDto convertToResponse(
            NomineeDetails nominee) {

        return NomineeDetailsResponseDto.builder()
                .nomineeId(nominee.getNomineeId())
                .policyId(nominee.getPolicyId())
                .nomineeName(nominee.getNomineeName())
                .relationship(nominee.getRelationship())
                .dob(nominee.getDob())
                .sharePercentage(nominee.getSharePercentage())
                .guardianName(nominee.getGuardianName())
                .build();
    }
}
