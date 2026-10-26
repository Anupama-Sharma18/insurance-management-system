package policy_servicing_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import policy_servicing_service.entity.ServiceRequestHistory;

import java.util.List;

public interface ServiceRequestHistoryRepository
        extends JpaRepository<ServiceRequestHistory, Long> {
    List<ServiceRequestHistory> findByRequestId(Long requestId);
}