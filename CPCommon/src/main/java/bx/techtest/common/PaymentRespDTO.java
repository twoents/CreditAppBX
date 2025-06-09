package bx.techtest.common;

import java.math.BigDecimal;
import java.util.Optional;

public class PaymentRespDTO {
    private Long loanId;
    private BigDecimal amountRemaining;
    private LoanStatus loanStatus;
    private String message;

    /**
     * @return the loanId
     */
    public Long getLoanId() {
        return loanId;
    }

    /**
     * @param loanId the loanId to set
     */
    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    /**
     * @return the amountRemaining
     */
    public BigDecimal getAmountRemaining() {
        return(amountRemaining );
    }

    /**
     * @param amountRemaining the amountRemaining to set
     */
    public void setAmountRemaining(BigDecimal amountRemaining) {
        this.amountRemaining = amountRemaining;
    }

    /**
     * @return the loanStatus
     */
    public LoanStatus getLoanStatus() {
        return loanStatus;
    }

    /**
     * @param loanStatus the loanStatus to set
     */
    public void setLoanStatus(LoanStatus loanStatus) {
        this.loanStatus = loanStatus;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
    
    
}