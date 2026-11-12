package com.cardamom.ledger;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CardamomLedgerApplicationTests {

    @Test
    @DisplayName("Verify Spring application context loads and Flyway migrations succeed")
    void contextLoads() {
    }
}
