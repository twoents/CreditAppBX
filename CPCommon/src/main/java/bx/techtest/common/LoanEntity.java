package bx.techtest.common;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table( name = "loans" )
public class LoanEntity {
    
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    
    private Long loanId;
    private BigDecimal loanAmount;
    private int term;
    private LoanStatus status;
    

    public Long getLoanId() {
        return loanId;
    }
    public void setLoanId(Long loadId) {
        this.loanId = loadId;
    }
    public BigDecimal getLoanAmount() {
        return loanAmount;
    }
    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }
    public int getTerm() {
        return term;
    }
    public void setTerm(int term) {
        this.term = term;
    }
    public LoanStatus getStatus() {
        return status;
    }
    public void setStatus(LoanStatus status) {
        this.status = status;
    }
}
