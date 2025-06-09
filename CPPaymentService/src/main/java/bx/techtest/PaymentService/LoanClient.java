package bx.techtest.PaymentService;

import bx.techtest.common.LoanEntity;
import bx.techtest.common.LoanStatus;
import bx.techtest.common.StatusUpdateDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class LoanClient {
    
    private RestTemplate restTemplate;
    private String baseUrl;

    public LoanClient( RestTemplate restTemplate, @Value("${base-url:http://localhost:8090/CreditProvider/}") String baseUrl ) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }    
    
    public LoanEntity getLoan( Long loanId ) {
        LoanEntity le = restTemplate.getForObject( baseUrl + "/loans/" + loanId , LoanEntity.class );
        return( le );
    }
    
    public LoanEntity updateLoanStatus( Long loanId, LoanStatus loanStatus ) {
        
        StatusUpdateDTO statusDto = new StatusUpdateDTO();
        statusDto.setLoanId(loanId);
        statusDto.setLoanStatus(loanStatus);
                
        LoanEntity le = restTemplate.postForObject( baseUrl + "/updateStatus", statusDto, LoanEntity.class );
        return( le );
    }
}