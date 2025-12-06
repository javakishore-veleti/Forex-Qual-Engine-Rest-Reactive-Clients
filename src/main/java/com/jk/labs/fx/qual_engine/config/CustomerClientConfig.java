package com.jk.labs.fx.qual_engine.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CustomerClientConfig {

    @Bean("customerRestClient")
    public RestClient customerRestClient(
            @Value("${integrations.customer.base-url}") String baseUrl) {

        return RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    @Bean("customerRestTemplate")
    public RestTemplate customerRestTemplate() {
        return new RestTemplate();
    }

    @Bean("customerBaseUrl")
    public String customerBaseUrl(@Value("${integrations.customer.base-url}") String baseUrl) {
        return baseUrl;
    }
}