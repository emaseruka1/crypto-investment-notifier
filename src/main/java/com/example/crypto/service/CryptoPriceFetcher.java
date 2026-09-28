package com.example.crypto.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CryptoPriceFetcher {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public BigDecimal getCurrentCryptoPrice(String symbol) throws Exception {

        String url = "https://api.binance.com/api/v3/ticker/price?symbol=" + symbol;

        String response = java.net.http.HttpClient.newHttpClient()
                .send(
                        java.net.http.HttpRequest.newBuilder()
                                .uri(java.net.URI.create(url))
                                .GET()
                                .build(),
                        java.net.http.HttpResponse.BodyHandlers.ofString()
                )
                .body();

        JsonNode json = objectMapper.readTree(response);

        return new BigDecimal(json.get("price").asText());
    }
}
