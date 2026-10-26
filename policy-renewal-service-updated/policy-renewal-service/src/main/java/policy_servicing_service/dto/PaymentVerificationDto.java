package policy_servicing_service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentVerificationDto {
    @NotNull(message = "paymentSuccessful is required")
    private Boolean paymentSuccessful;
}
