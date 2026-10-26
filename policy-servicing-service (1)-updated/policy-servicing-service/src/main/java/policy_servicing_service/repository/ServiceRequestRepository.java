
package policy_servicing_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import policy_servicing_service.entity.ServiceRequest;

public interface ServiceRequestRepository
        extends JpaRepository<ServiceRequest, Long> {

}
