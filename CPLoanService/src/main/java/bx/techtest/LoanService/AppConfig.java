package bx.techtest.LoanService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.testcontainers.shaded.org.checkerframework.checker.interning.qual.CompareToMethod;

@Component
public class AppConfig {

    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        logger.info("LoanServerReady");
    }
}
