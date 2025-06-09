package bx.techtest.PaymentService;

import bx.techtest.common.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/CreditProvider")
public class PaymentController {
    
    @Autowired
    PaymentService paymentService;
    
    @PostMapping( "/payments" )
    public PaymentRespDTO processPayment( @RequestBody PaymentDTO paymentDto ) throws Exception {
        return( paymentService.processPayment( paymentDto ) );
    }
}