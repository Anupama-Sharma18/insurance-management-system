package policy_servicing_service.service;

import java.time.LocalDate;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import policy_servicing_service.dto.PolicyDetailsDto;

/**
 * Adapter for the Policy Insurance microservice.
 * Policy Insurance must be running at policy.service.url.
 */
@Service
public class PolicyDataService {
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public PolicyDataService(
            @Value("${policy.service.url:http://localhost:8088}") String policyServiceUrl,
            ObjectMapper objectMapper) {
        this.restClient = RestClient.builder().baseUrl(policyServiceUrl).build();
        this.objectMapper = objectMapper;
    }

    public PolicyDetailsDto getPolicyById(Long policyId) {
        JsonNode response = restClient.get()
                .uri("/api/v1/policies/{id}", policyId)
                .retrieve()
                .body(JsonNode.class);
        JsonNode data = response == null ? null : response.get("data");
        if (data == null || data.isNull()) {
            throw new IllegalStateException("Policy service returned no policy data for ID " + policyId);
        }
        return objectMapper.convertValue(data, PolicyDetailsDto.class);
    }

    public PolicyDetailsDto renewPolicy(Long policyId, LocalDate newExpiryDate) {
        restClient.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/policies/{id}/renewal-expiry")
                        .queryParam("newExpiryDate", newExpiryDate)
                        .build(policyId))
                .retrieve()
                .toBodilessEntity();
        return getPolicyById(policyId);
    }

    public PolicyDetailsDto cancelPolicy(Long policyId) {
        restClient.delete()
                .uri("/api/v1/policies/{id}", policyId)
                .retrieve()
                .toBodilessEntity();
        return getPolicyById(policyId);
    }
}
