package bx.techtest.PaymentService;

import bx.techtest.common.LoanEntity;
import bx.techtest.common.LoanStatus;
import bx.techtest.common.PaymentDTO;
import bx.techtest.common.PaymentEntity;
import bx.techtest.common.PaymentRespDTO;
import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    
    @Autowired
    private PaymentRepository paymentRepository;    
    
    @Autowired
    private LoanClient loanClient;
    
    public Optional<PaymentEntity> getPayment( Long loanId ) {
        return( paymentRepository.findById(loanId) );
    }
    
    public PaymentRespDTO processPayment( PaymentDTO paymentDto ) throws Exception {
        
        PaymentRespDTO response = new PaymentRespDTO();
        response.setLoanId( paymentDto.getLoanId() );
        
        LoanEntity loan = loanClient.getLoan(paymentDto.getLoanId());
        if ( loan == null ) {
            response.setMessage( "Invalid loanId specified, loan does not exist" );
            return( response );
        }
        
        BigDecimal repaymentTotal = paymentRepository.getRepaymentTotal(paymentDto.getLoanId() );
        if ( repaymentTotal.add(paymentDto.getPaymentAmount()).compareTo(loan.getLoanAmount() ) > 0  ) {
            response.setMessage( "Payment amount too large, total payments would exceed loan amount" );
            return( response );
        }
        
        PaymentEntity payment = new PaymentEntity();
        payment.setLoanId( paymentDto.getLoanId() );
        payment.setPaymentAmount( paymentDto.getPaymentAmount() );
        PaymentEntity updatedEntity = paymentRepository.save( payment );
        
        repaymentTotal = paymentRepository.getRepaymentTotal(paymentDto.getLoanId() );
        response.setAmountRemaining(loan.getLoanAmount().subtract(repaymentTotal));
        response.setLoanStatus(loan.getStatus() );
        
        if ( repaymentTotal.compareTo(loan.getLoanAmount() ) == 0 ) {
            LoanEntity updatedLoan = loanClient.updateLoanStatus( loan.getLoanId(), LoanStatus.SETTLED );
            response.setLoanStatus(updatedLoan.getStatus() );
        }
        
        return( response );
    }    
}
