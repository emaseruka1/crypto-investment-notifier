package com.example.crypto.service;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReturnCalculator {

    private BinanceDataFetcher binanceDataFetcher;
    private CryptoPriceFetcher cryptoPriceFetcher;

    BigDecimal initialInvestment = new BigDecimal("2286.93493");
    BigDecimal entryEthPrice = new BigDecimal("3722.49");
    BigDecimal guvnorSaccoEth = new BigDecimal("0.1409589");

    public ReturnCalculator(
            BinanceDataFetcher binanceDataFetcher,
            CryptoPriceFetcher cryptoPriceFetcher) {

        this.binanceDataFetcher = binanceDataFetcher;
        this.cryptoPriceFetcher = cryptoPriceFetcher;
    }

    @SneakyThrows
    public Map<String, BigDecimal> calculatePortfolioReturn(){

        Map<String, BigDecimal> portfolioReturn = new HashMap<>();

        BigDecimal ethPriceToday =
                cryptoPriceFetcher.getCurrentCryptoPrice("ETHUSDT");

        Map<String, BigDecimal> balances =
                binanceDataFetcher.getBalances();


        BigDecimal portfolioValue =
                balances.get("ETH")
                        .subtract(guvnorSaccoEth)
                        .multiply(ethPriceToday)
                        .add(balances.get("USDT"));


        BigDecimal portfolioReturnValue =
                portfolioValue
                        .divide(initialInvestment, 10, RoundingMode.HALF_UP)
                        .subtract(BigDecimal.ONE)
                        .multiply(new BigDecimal("100"))
                        .setScale(2, RoundingMode.HALF_UP);

        BigDecimal benchmarkReturn =
                ethPriceToday
                        .divide(entryEthPrice, 10, RoundingMode.HALF_UP)
                        .subtract(BigDecimal.ONE)
                        .multiply(new BigDecimal("100"))
                        .setScale(2, RoundingMode.HALF_UP);

        BigDecimal alphaReturn =
                portfolioReturnValue.subtract(benchmarkReturn);

        portfolioReturn.put("portfolioValue", portfolioValue);
        portfolioReturn.put("return", portfolioReturnValue);
        portfolioReturn.put("benchmark", benchmarkReturn);
        portfolioReturn.put("alpha", alphaReturn);

        return portfolioReturn;
    }
}