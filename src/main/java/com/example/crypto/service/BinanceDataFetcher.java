package com.example.crypto.service;

import com.binance.connector.client.common.ApiResponse;
import com.binance.connector.client.common.configuration.ClientConfiguration;
import com.binance.connector.client.common.configuration.SignatureConfiguration;
import com.binance.connector.client.spot.rest.api.SpotRestApi;
import com.binance.connector.client.spot.rest.model.GetAccountResponse;
import com.example.crypto.controller.RunJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class BinanceDataFetcher {

    private static final Logger log = LoggerFactory.getLogger(BinanceDataFetcher.class);

    private final SpotRestApi spotApi;

    public BinanceDataFetcher( @Value("${binance.key}") String binanceKey,
                               @Value("${binance.secret}") String binanceSecret) {

        SignatureConfiguration signature = new SignatureConfiguration();
        signature.setApiKey(binanceKey);
        signature.setSecretKey(binanceSecret);

        ClientConfiguration config = new ClientConfiguration();
        config.setSignatureConfiguration(signature);

        spotApi = new SpotRestApi(config);
    }

    public GetAccountResponse getAccount() {

        log.info("Calling Binance getAccount...");

        ApiResponse<GetAccountResponse> response = spotApi.getAccount(true,null);

        log.info("Binance response received...");

        return response.getData();

    }

    public Map<String, BigDecimal> getBalances(){

        Map<String,BigDecimal> balances = new HashMap<>();;

        GetAccountResponse account = getAccount();

        account.getBalances().forEach(balance -> {

            if (balance.getAsset().equals("ETH") ||
                    balance.getAsset().equals("USDT")) {

                balances.put(balance.getAsset(), new BigDecimal(balance.getFree()));
            }
        });
        return balances;
    }

}
