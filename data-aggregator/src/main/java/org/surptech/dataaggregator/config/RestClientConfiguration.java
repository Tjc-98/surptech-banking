package org.surptech.dataaggregator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.surptech.common.client.RestClientBuilder;

import java.time.Duration;

@Configuration
public class RestClientConfiguration {

    @Value("${service.customer-profile.url}")
    private String customerProfileUrl;

    @Value("${service.credit-profile.url}")
    private String creditProfileUrl;

    @Value("${service.auth-server.url}")
    private String authServerUrl;

    @Bean(name = "customerProfileRestClient")
    public RestClient customerProfileRestClient() {
        return RestClientBuilder.create()
                .baseUrl(customerProfileUrl)
                .connectTimeout(Duration.ofSeconds(5))
                .readTimeout(Duration.ofSeconds(10))
                .enableLogging(true)
                .build();
    }

    @Bean(name = "creditProfileRestClient")
    public RestClient creditProfileRestClient() {
        return RestClientBuilder.create()
                .baseUrl(creditProfileUrl)
                .connectTimeout(Duration.ofSeconds(5))
                .readTimeout(Duration.ofSeconds(10))
                .enableLogging(true)
                .build();
    }

    @Bean(name = "authServerRestClient")
    public RestClient authServerRestClient() {
        return RestClientBuilder.create()
                .baseUrl(authServerUrl)
                .connectTimeout(Duration.ofSeconds(5))
                .readTimeout(Duration.ofSeconds(10))
                .enableLogging(true)
                .build();
    }
}
