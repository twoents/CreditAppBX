package bx.techtest.DatabaseService;

import org.h2.tools.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;
import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;

@Configuration
public class DatabaseServiceConfig {
    
    private Server h2ServerInstance;
    
    @Bean
    public Server h2TcpServer() throws SQLException {
        // Start H2 TCP server on port 9092, allowing connections from other services
        h2ServerInstance = Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9090").start();
        return( h2ServerInstance );
    }
    
    @EventListener(ContextClosedEvent.class)
    public void stopH2Server() {
        if (h2ServerInstance != null && h2ServerInstance.isRunning(true)) {
            h2ServerInstance.stop();
        }
    }    

    private static final Logger logger = LoggerFactory.getLogger(DatabaseServiceConfig.class);

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        logger.info("DatabaseServerReady");
    }

}