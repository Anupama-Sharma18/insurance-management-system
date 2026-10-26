package policy_servicing_service.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import policy_servicing_service.entity.PolicyCancellation;

public interface PolicyCancellationRepository
        extends JpaRepository<PolicyCancellation, Long> {

    List<PolicyCancellation> findByPolicyId(Long policyId);

    boolean existsByPolicyId(Long policyId);
}
