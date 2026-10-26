
package policy_servicing_service.service;

import java.time.LocalDate;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import policy_servicing_service.dto.PolicyDetailsDto;
import policy_servicing_service.exception.ResourceNotFoundException;

/**
 * Adapter for the Policy Issuance microservice.
 * Policy Issuance must be running at policy.service.url.
 */
@Service
public class PolicyDataService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public PolicyDataService(
            @Value("${policy.service.url:http://localhost:8088}")
            String policyServiceUrl,
            ObjectMapper objectMapper) {

        this.restClient = RestClient.builder()
                .baseUrl(policyServiceUrl)
                .build();

        this.objectMapper = objectMapper;
    }

    public PolicyDetailsDto getPolicyById(Long policyId) {

        try {
            JsonNode response = restClient.get()
                    .uri("/api/v1/policies/{id}", policyId)
                    .retrieve()
                    .body(JsonNode.class);

            JsonNode data =
                    response == null ? null : response.get("data");

            if (data == null || data.isNull()) {
                throw new IllegalStateException(
                        "Policy service returned no policy data for ID "
                                + policyId);
            }

            return objectMapper.convertValue(
                    data, PolicyDetailsDto.class);

        } catch (HttpClientErrorException.NotFound ex) {
            throw new ResourceNotFoundException(
                    "Policy not found: " + policyId);
        }
    }

    public PolicyDetailsDto renewPolicy(
            Long policyId, LocalDate newExpiryDate) {

        try {
            restClient.put()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/v1/policies/{id}/renewal-expiry")
                            .queryParam("newExpiryDate", newExpiryDate)
                            .build(policyId))
                    .retrieve()
                    .toBodilessEntity();

            return getPolicyById(policyId);

        } catch (HttpClientErrorException.NotFound ex) {
            throw new ResourceNotFoundException(
                    "Policy not found: " + policyId);

        } catch (HttpClientErrorException.BadRequest ex) {
            throw new IllegalArgumentException(
                    extractErrorMessage(ex));
        }
    }

    public PolicyDetailsDto cancelPolicy(Long policyId) {

        try {
            restClient.delete()
                    .uri("/api/v1/policies/{id}", policyId)
                    .retrieve()
                    .toBodilessEntity();

            return getPolicyById(policyId);

        } catch (HttpClientErrorException.NotFound ex) {
            throw new ResourceNotFoundException(
                    "Policy not found: " + policyId);
        }
    }

    private String extractErrorMessage(
            RestClientResponseException ex) {

        try {
            JsonNode body = objectMapper.readTree(
                    ex.getResponseBodyAsString());

            JsonNode message = body.get("message");

            if (message != null && !message.isNull()) {
                return message.asText();
            }

        } catch (Exception ignored) {
            // Use the original response status if the body isn't JSON.
        }

        return "Policy renewal request failed with status "
                + ex.getStatusCode().value();
    }
}
