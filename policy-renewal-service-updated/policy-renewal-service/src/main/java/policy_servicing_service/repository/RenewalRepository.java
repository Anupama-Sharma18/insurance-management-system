
package policy_servicing_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import policy_servicing_service.entity.Renewal;

public interface RenewalRepository extends JpaRepository<Renewal, Long> {

    List<Renewal> findByPolicyId(Long policyId);
}
