
package policy_servicing_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import policy_servicing_service.entity.Endorsement;

public interface EndorsementRepository
        extends JpaRepository<Endorsement, Long> {

}
