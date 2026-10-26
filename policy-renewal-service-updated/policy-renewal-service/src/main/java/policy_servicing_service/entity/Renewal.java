package policy_servicing_service.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import policy_servicing_service.enums.RenewalStatus;

@Entity
@Table(name = "policy_renewal")
public class Renewal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "renewal_id")
    private Long renewalId;

    @Column(name = "policy_id", nullable = false)
    private Long policyId;

    @Column(name = "renewal_date", nullable = false)
    private LocalDate renewalDate;

    @Column(name = "new_expiry_date", nullable = false)
    private LocalDate newExpiryDate;

    @Column(name = "premium_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal premiumAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "renewal_status", nullable = false)
    private RenewalStatus renewalStatus = RenewalStatus.PENDING;

    @Column(name = "reminder_sent", nullable = false)
    private boolean reminderSent = false;

    @Column(name = "customer_confirmed", nullable = false)
    private boolean customerConfirmed = false;

    @Column(name = "payment_verified", nullable = false)
    private boolean paymentVerified = false;

    public Renewal() {}
    public Long getRenewalId() { return renewalId; }
    public void setRenewalId(Long renewalId) { this.renewalId = renewalId; }
    public Long getPolicyId() { return policyId; }
    public void setPolicyId(Long policyId) { this.policyId = policyId; }
    public LocalDate getRenewalDate() { return renewalDate; }
    public void setRenewalDate(LocalDate renewalDate) { this.renewalDate = renewalDate; }
    public LocalDate getNewExpiryDate() { return newExpiryDate; }
    public void setNewExpiryDate(LocalDate newExpiryDate) { this.newExpiryDate = newExpiryDate; }
    public BigDecimal getPremiumAmount() { return premiumAmount; }
    public void setPremiumAmount(BigDecimal premiumAmount) { this.premiumAmount = premiumAmount; }
    public RenewalStatus getRenewalStatus() { return renewalStatus; }
    public void setRenewalStatus(RenewalStatus renewalStatus) { this.renewalStatus = renewalStatus; }
    public boolean isReminderSent() { return reminderSent; }
    public void setReminderSent(boolean reminderSent) { this.reminderSent = reminderSent; }
    public boolean isCustomerConfirmed() { return customerConfirmed; }
    public void setCustomerConfirmed(boolean customerConfirmed) { this.customerConfirmed = customerConfirmed; }
    public boolean isPaymentVerified() { return paymentVerified; }
    public void setPaymentVerified(boolean paymentVerified) { this.paymentVerified = paymentVerified; }
}
