
package policy_servicing_service.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "nominee_details")
public class NomineeDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nominee_id")
    private Long nomineeId;

    @Column(name = "policy_id")
    private Long policyId;

    @Column(name = "nominee_name", length = 100)
    private String nomineeName;

    @Column(name = "relationship", length = 50)
    private String relationship;

    @Column(name = "dob")
    private LocalDate dob;

    @Column(name = "share_percentage", precision = 5, scale = 2)
    private BigDecimal sharePercentage;

    @Column(name = "guardian_name", length = 100)
    private String guardianName;

    public NomineeDetails() {
    }

    public Long getNomineeId() {
        return nomineeId;
    }

    public void setNomineeId(Long nomineeId) {
        this.nomineeId = nomineeId;
    }

    public Long getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Long policyId) {
        this.policyId = policyId;
    }

    public String getNomineeName() {
        return nomineeName;
    }

    public void setNomineeName(String nomineeName) {
        this.nomineeName = nomineeName;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public BigDecimal getSharePercentage() {
        return sharePercentage;
    }

    public void setSharePercentage(BigDecimal sharePercentage) {
        this.sharePercentage = sharePercentage;
    }

    public String getGuardianName() {
        return guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }
}
