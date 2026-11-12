package com.cardamom.ledger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
public class CardamomLedgerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CardamomLedgerApplication.class, args);
    }
}
