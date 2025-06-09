package bx.techtest.LoanService;

import bx.techtest.common.LoanEntity;
import bx.techtest.common.LoanStatus;
import bx.techtest.common.StatusUpdateDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/CreditProvider")
public class LoanController {

    @Autowired
    private LoanService loanService;
    
    @GetMapping( "/loans" )
    public List<LoanEntity> geAllUsers() {
        return( loanService.getAllUsers() );
    }
    
    @GetMapping( "/loans/{loanId}" )
    public Optional<LoanEntity> getLoan( @PathVariable Long loanId ) {
        return( loanService.getLoan(loanId));
    }
    
    @PostMapping( "/updateStatus" ) 
    public LoanEntity updateStatus( @RequestBody StatusUpdateDTO statusUpdate ) {
        return( loanService.updateLoanStatus( statusUpdate.getLoanId(), statusUpdate.getLoanStatus() ));
    }
    
    @PostMapping( "/loans" )
    public Optional<LoanEntity> createLoan( @RequestBody LoanEntity loan ) {
        return( loanService.createLoan( loan ) );
    }
    
}
