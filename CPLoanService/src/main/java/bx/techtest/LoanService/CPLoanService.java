package bx.techtest.LoanService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan( "bx.techtest.common" )
public class CPLoanService {

	public static void main(String[] args) {
		SpringApplication.run(CPLoanService.class, args);
	}

}
