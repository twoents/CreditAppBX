package bx.techtest.PaymentService;

import bx.techtest.common.PaymentEntity;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends CrudRepository<PaymentEntity, Long>{

    @Query("SELECT payment FROM PaymentEntity payment WHERE payment.loanId = :value1" )
    List<PaymentEntity> findByLoanId( @Param("value1") Long value1 );

    @Query("select coalesce(sum(payment.paymentAmount),0) from PaymentEntity payment WHERE payment.loanId = :loan_id_value" )
    BigDecimal getRepaymentTotal( @Param( "loan_id_value") Long loanId );
    
}

