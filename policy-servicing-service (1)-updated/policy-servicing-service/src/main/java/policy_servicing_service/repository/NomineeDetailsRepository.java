
package policy_servicing_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import policy_servicing_service.entity.NomineeDetails;

import java.util.List;

public interface NomineeDetailsRepository
        extends JpaRepository<NomineeDetails, Long> {

    List<NomineeDetails> findByPolicyId(Long policyId);
}
