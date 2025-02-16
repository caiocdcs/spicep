package com.example.spicep.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@Data
@ConfigurationProperties("coin-cap")
public class CoinCapConfiguration {

    private String url;

    @Bean("coinCapRestClient")
    public RestClient coinCapRestClient() {
        return RestClient.builder()
                .baseUrl(url)
                .build();
    }
}
