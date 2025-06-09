package bx.techtest.LoanService;

import bx.techtest.common.LoanEntity;
import bx.techtest.common.LoanStatus;
import bx.techtest.common.LoanStatusConverter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.MountableFile;

@SpringBootTest
@EntityScan( "bx.techtest.common" )
@TestMethodOrder(OrderAnnotation.class)
public class LoanServiceTest {
    
    @Autowired
    JdbcTemplate jdbcTemplate;
  
    @Autowired
    LoanService loanService;
    
    static {
        GenericContainer<?> aaContainer = new GenericContainer<>("openjdk:21")
                .withCopyFileToContainer(
                    MountableFile.forHostPath("target/testcontainers/CPDatabaseService.jar"),
                    "/app.jar"
                )
                .withExposedPorts(8080, 9090)
                .withCommand("java", "-jar", "/app.jar")
                .waitingFor( Wait.forLogMessage(".*DatabaseServerReady.*", 1) );

            List<String> portBindings = new ArrayList<>();
            portBindings.add("8080:8080"); // hostPort:containerPort
            portBindings.add("9090:9090"); // hostPort:containerPort
            aaContainer.setPortBindings(portBindings);
            
            aaContainer.start();
    }
    
    @Test
    @Order(1)
    public void testDbExists() {
        
        String sql = "select count(*) "
                   + "from loans ";
        Long loanCount = jdbcTemplate.queryForObject( sql, Long.class );
        assertNotNull( loanCount );
    }
    
    @Test
    @Order(2)
    public void testLoanCreate() {
        
        LoanEntity loan = new LoanEntity();
        loan.setLoanAmount( BigDecimal.valueOf(12030) );
        loan.setTerm( 12 );
        loan.setStatus( LoanStatus.ACTIVE );
        LoanEntity retLoan = loanService.createLoan( loan ).get();
        assertNotNull( retLoan );
        
        LoanStatusConverter statusConverter = new LoanStatusConverter();
        
        String sql = "select * "
                   + "from loans "
                   + "where ( loan_id = ? ) ";
        Map<String, Object> loanMap = jdbcTemplate.queryForMap( sql, new Object[] { retLoan.getLoanId() } );
        assertEquals( loanMap.get( "loan_id" ), retLoan.getLoanId() );
        assertThat( ( ( BigDecimal )loanMap.get( "loan_amount" ) ).compareTo( retLoan.getLoanAmount() ) == 0 );
        assertEquals( loanMap.get( "term" ), retLoan.getTerm() );
        assertEquals( loanMap.get( "status" ), statusConverter.convertToDatabaseColumn( retLoan.getStatus() ) );
    }    
}
