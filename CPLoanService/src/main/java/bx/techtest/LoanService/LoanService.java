package bx.techtest.LoanService;

import bx.techtest.common.LoanEntity;
import bx.techtest.common.LoanStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanService {
    
    @Autowired
    private LoanRepository loanRepository;
    
    public List<LoanEntity> getAllUsers() {
        return ( (List<LoanEntity>) ( loanRepository.findAll()) );
    }
    
    public Optional<LoanEntity> getLoan( Long loanId ) {
        return( loanRepository.findById(loanId) );
    }
    
    public Optional<LoanEntity> createLoan( LoanEntity loan ) {
        LoanEntity loanEntity = null;
        if ( ( loanEntity = loanRepository.save( loan ) ) == null ) {
            return( Optional.empty() );
        };
        return( Optional.of( loanEntity ) );
    }
    
    public LoanEntity updateLoanStatus( Long loanId, LoanStatus newStatus ) {
        LoanEntity loan = loanRepository.findById(loanId).get();
        loan.setStatus(newStatus);
        LoanEntity updatedLoan = loanRepository.save(loan);
        return( updatedLoan );
    }
}
