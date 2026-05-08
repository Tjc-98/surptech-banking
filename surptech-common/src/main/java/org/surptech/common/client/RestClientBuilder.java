package org.surptech.common.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder for creating configured RestClient instances with common settings.
 * Provides fluent API for configuring base URL, timeouts, interceptors, and error handling.
 */
@Slf4j
public class RestClientBuilder {

    private String baseUrl;
    private Duration connectTimeout = Duration.ofSeconds(5);
    private Duration readTimeout = Duration.ofSeconds(10);
    private final List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
    private boolean enableLogging = true;

    private RestClientBuilder() {
    }

    /**
     * Creates a new RestClientBuilder instance.
     *
     * @return new RestClientBuilder
     */
    public static RestClientBuilder create() {
        return new RestClientBuilder();
    }

    /**
     * Sets the base URL for the RestClient.
     *
     * @param baseUrl the base URL
     * @return this builder
     */
    public RestClientBuilder baseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    /**
     * Sets the connection timeout.
     *
     * @param connectTimeout connection timeout duration
     * @return this builder
     */
    public RestClientBuilder connectTimeout(Duration connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    /**
     * Sets the read timeout.
     *
     * @param readTimeout read timeout duration
     * @return this builder
     */
    public RestClientBuilder readTimeout(Duration readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    /**
     * Adds a custom interceptor to the RestClient.
     *
     * @param interceptor the interceptor to add
     * @return this builder
     */
    public RestClientBuilder addInterceptor(ClientHttpRequestInterceptor interceptor) {
        this.interceptors.add(interceptor);
        return this;
    }

    /**
     * Enables or disables request/response logging.
     *
     * @param enableLogging true to enable logging, false to disable
     * @return this builder
     */
    public RestClientBuilder enableLogging(boolean enableLogging) {
        this.enableLogging = enableLogging;
        return this;
    }

    /**
     * Builds and returns the configured RestClient.
     *
     * @return configured RestClient instance
     */
    public RestClient build() {
        if (baseUrl == null || baseUrl.isEmpty()) {
            throw new IllegalStateException("Base URL must be set");
        }

        log.info("Building RestClient with baseUrl: {}, connectTimeout: {}ms, readTimeout: {}ms",
                baseUrl, connectTimeout.toMillis(), readTimeout.toMillis());

        RestClient.Builder builder = RestClient.builder()
                .baseUrl(baseUrl);

        // Add logging interceptor if enabled
        if (enableLogging) {
            interceptors.add(0, new RestClientLoggingInterceptor());
        }

        // Add all interceptors
        if (!interceptors.isEmpty()) {
            builder.requestInterceptors(interceptorList -> interceptorList.addAll(interceptors));
        }

        return builder.build();
    }
}
