package bx.techtest.PaymentService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan( "bx.techtest.common" )
public class CPPaymentService {

	public static void main(String[] args) {
		SpringApplication.run(CPPaymentService.class, args);
	}

}