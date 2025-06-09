package bx.techtest.common;

import java.math.BigDecimal;

public class PaymentDTO {
    private Long loanId;
    private BigDecimal paymentAmount;

    public Long getLoanId() {
        return loanId;
    }
    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }
    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }
    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }
}
