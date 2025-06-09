package bx.techtest.PaymentService;

import bx.techtest.common.LoanStatus;
import bx.techtest.common.PaymentDTO;
import bx.techtest.common.PaymentRespDTO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.MethodOrderer.*;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.MountableFile;

@SpringBootTest
@EntityScan( "bx.techtest.common" )
@TestMethodOrder(OrderAnnotation.class)
public class PaymentServiceTest {
    
    @Autowired
    JdbcTemplate jdbcTemplate;
  
    @Autowired
    PaymentService paymentService;
    
    static {
        Network network = Network.newNetwork();        
        
        GenericContainer<?> dbsContainer = new GenericContainer<>("openjdk:21")
            .withCopyFileToContainer(
                MountableFile.forHostPath("target/testcontainers/CPDatabaseService.jar"), "/app.jar" )
                .withExposedPorts(8080, 9090)
                .withNetwork(network)
                .withNetworkAliases( "aa" )
                .withCommand("java", "-jar", "/app.jar")
                .waitingFor( Wait.forLogMessage(".*DatabaseServerReady.*", 1)
            );

        List<String> portBindings = new ArrayList<>();
        portBindings.add("8080:8080"); // hostPort:containerPort
        portBindings.add("9090:9090"); // hostPort:containerPort
        dbsContainer.setPortBindings(portBindings);
            
        GenericContainer<?> lsContainer = new GenericContainer<>("openjdk:21")
                .withCopyFileToContainer(
                    MountableFile.forHostPath("target/testcontainers/CPLoanService.jar"),
                    "/app.jar"
                )
                .withExposedPorts(8090)
                .withNetwork(network)
                .withEnv( "DB_HOST", "aa" )
                .withCommand("java", "-jar", "/app.jar")
                .dependsOn( dbsContainer )
                .waitingFor( Wait.forLogMessage(".*LoanServerReady.*", 1) 
            );

        List<String> portBindings2 = new ArrayList<>();
        portBindings2.add("8090:8090"); // hostPort:containerPort
        lsContainer.setPortBindings(portBindings2);

        dbsContainer.start();
        lsContainer.start();
    }
    
    @Test
    @Order(1)
    public void testDbExists() {
        
        String sql = "select count(*) "
                   + "from payments ";
        Long loanCount = jdbcTemplate.queryForObject( sql, Long.class );
        assertNotNull( loanCount );
    }

    @Test
    @Order(2)
    public void testInvalidLoan() {
        
        PaymentDTO paymentDto = new PaymentDTO();
        paymentDto.setLoanId( 99999999999L );
        paymentDto.setPaymentAmount(BigDecimal.valueOf( 100L ) );
        
        try {
            PaymentRespDTO paymentRespDto = paymentService.processPayment(paymentDto);
            assertNotNull(paymentRespDto );
            assertEquals( paymentRespDto.getMessage(), "Invalid loanId specified, loan does not exist" );
        }
        catch ( Exception e ) {
            assertNull( e );
        }
    }
    
    @Test
    @Order(3)
    public void testAmountOk() {
        
        PaymentDTO paymentDto = new PaymentDTO();
        paymentDto.setLoanId( 1L );
        paymentDto.setPaymentAmount(BigDecimal.valueOf( 5000L ) );
        
        try {
            PaymentRespDTO paymentRespDto = paymentService.processPayment(paymentDto);
            assertNotNull(paymentRespDto );
            Assertions.assertThat( paymentRespDto.getAmountRemaining().compareTo(BigDecimal.valueOf(5000L)) == 0 );
            assertEquals( paymentRespDto.getLoanStatus(), LoanStatus.ACTIVE );
        }
        catch ( Exception e ) {
            assertNull( e );
        }
    }
    
    @Test
    @Order(3)
    public void testAmountExceedLoan() {
        
        PaymentDTO paymentDto = new PaymentDTO();
        paymentDto.setLoanId( 2L );
        paymentDto.setPaymentAmount(BigDecimal.valueOf( 5001L ) );
        
        try {
            PaymentRespDTO paymentRespDto = paymentService.processPayment(paymentDto);
            assertNotNull(paymentRespDto );
            assertEquals( paymentRespDto.getMessage(), "Payment amount too large, total payments would exceed loan amount" );
        }
        catch ( Exception e ) {
            assertNull( e );
        }
    }
    
    @Test
    @Order(3)
    public void testLoanSettledByPayment() {
        
        PaymentDTO paymentDto = new PaymentDTO();
        paymentDto.setLoanId( 2L );
        paymentDto.setPaymentAmount(BigDecimal.valueOf( 5000L ) );
        
        try {
            PaymentRespDTO paymentRespDto = paymentService.processPayment(paymentDto);
            assertNotNull(paymentRespDto );
            Assertions.assertThat( paymentRespDto.getAmountRemaining().compareTo(BigDecimal.ZERO) == 0 );
            assertEquals( paymentRespDto.getLoanStatus(), LoanStatus.SETTLED );
        }
        catch ( Exception e ) {
            assertNull( e );
        }
    }
}
