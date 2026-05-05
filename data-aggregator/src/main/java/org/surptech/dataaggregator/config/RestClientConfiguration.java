package org.surptech.dataaggregator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

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
        return RestClient.builder()
                .baseUrl(customerProfileUrl)
                .build();
    }

    @Bean(name = "creditProfileRestClient")
    public RestClient creditProfileRestClient() {
        return RestClient.builder()
                .baseUrl(creditProfileUrl)
                .build();
    }

    @Bean(name = "authServerRestClient")
    public RestClient authServerRestClient() {
        return RestClient.builder()
                .baseUrl(authServerUrl)
                .build();
    }
}
