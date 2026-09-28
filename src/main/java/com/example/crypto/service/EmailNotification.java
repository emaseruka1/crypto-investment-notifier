package com.example.crypto.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Service
public class EmailNotification {

    private final JavaMailSender mailSender;
    private final ReturnCalculator returnCalculator;

    @Value("${spring.mail.username}")
    private String senderEmail;

    public EmailNotification(
            JavaMailSender mailSender,
            ReturnCalculator returnCalculator) {
        this.mailSender = mailSender;
        this.returnCalculator = returnCalculator;
    }

    public void sendWeeklyUpdate() throws Exception {

        Map<String, BigDecimal> result =
                returnCalculator.calculatePortfolioReturn();

        BigDecimal portfolioValue = result.get("portfolioValue");
        BigDecimal portfolioReturn = result.get("return");
        BigDecimal benchmarkReturn = result.get("benchmark");
        BigDecimal alpha = result.get("alpha");

        BigDecimal initialInvestment = new BigDecimal("2286.93493");

        BigDecimal emma = new BigDecimal("1389.49")
                .divide(initialInvestment, 10, RoundingMode.HALF_UP)
                .multiply(portfolioValue)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal kevin = new BigDecimal("501.96")
                .divide(initialInvestment, 10, RoundingMode.HALF_UP)
                .multiply(portfolioValue)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal deborah = new BigDecimal("394.91")
                .divide(initialInvestment, 10, RoundingMode.HALF_UP)
                .multiply(portfolioValue)
                .setScale(2, RoundingMode.HALF_UP);

        String[] receivers = {
                "emmamaseruka97@gmail.com"
        };

        String body = """
                Dear Investor,

                Here is the latest weekly update for the entire Cryptocurrency fund:

                Mr. Maseruka invested USD 1,389.49 and the investment is now worth USD %s

                Mr. Meng invested USD 501.96 and the investment is now worth USD %s

                Ms. Deborah invested USD 394.91 and the investment is now worth USD %s


                1. Ethereum Benchmark Return:
                %s%%

                2. Our Fund Return:
                %s%%

                3. Alpha:
                %s%%


                If Alpha is positive, it indicates that our fund is performing better than the market.
                If Alpha is negative, it indicates that our fund is performing worse than the market.


                Please reach out in case of any questions.

                Best regards,
                EQUINOX 2.0
                """.formatted(
                emma,
                kevin,
                deborah,
                benchmarkReturn,
                portfolioReturn,
                alpha
        );

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(senderEmail);
        message.setTo(receivers);
        message.setSubject("EQUINOX 2 Trading Robot Update");
        message.setText(body);

        mailSender.send(message);
    }
}