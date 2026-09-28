package com.example.crypto;

import com.example.crypto.service.BinanceDataFetcher;
import com.example.crypto.service.EmailNotification;
import com.example.crypto.service.ReturnCalculator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.Map;

@SpringBootApplication
public class CryptoInvestmentNotifier {

    public static void main(String[] args) {
        SpringApplication.run(CryptoInvestmentNotifier.class, args);
    }

    @Bean
    CommandLineRunner run(EmailNotification emailNotification) {
        return args -> {
            emailNotification.sendWeeklyUpdate();
            System.out.println("Email sent successfully");
        };
    }
}













