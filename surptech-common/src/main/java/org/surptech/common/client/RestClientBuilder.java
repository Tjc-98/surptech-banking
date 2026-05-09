package org.surptech.common.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
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
     * URL must be non-empty and start with http:// or https://.
     *
     * @param baseUrl the base URL
     * @return this builder
     * @throws IllegalArgumentException if URL is invalid
     */
    public RestClientBuilder baseUrl(String baseUrl) {
        if (baseUrl == null || baseUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("Base URL cannot be null or empty");
        }
        String trimmedUrl = baseUrl.trim();
        if (!trimmedUrl.startsWith("http://") && !trimmedUrl.startsWith("https://")) {
            throw new IllegalArgumentException("Base URL must start with http:// or https://");
        }
        this.baseUrl = trimmedUrl;
        return this;
    }

    /**
     * Sets the connection timeout.
     * Timeout must be positive.
     *
     * @param connectTimeout connection timeout duration
     * @return this builder
     * @throws IllegalArgumentException if timeout is null, zero, or negative
     */
    public RestClientBuilder connectTimeout(Duration connectTimeout) {
        if (connectTimeout == null) {
            throw new IllegalArgumentException("Connect timeout cannot be null");
        }
        if (connectTimeout.isNegative() || connectTimeout.isZero()) {
            throw new IllegalArgumentException("Connect timeout must be positive");
        }
        this.connectTimeout = connectTimeout;
        return this;
    }

    /**
     * Sets the read timeout.
     * Timeout must be positive.
     *
     * @param readTimeout read timeout duration
     * @return this builder
     * @throws IllegalArgumentException if timeout is null, zero, or negative
     */
    public RestClientBuilder readTimeout(Duration readTimeout) {
        if (readTimeout == null) {
            throw new IllegalArgumentException("Read timeout cannot be null");
        }
        if (readTimeout.isNegative() || readTimeout.isZero()) {
            throw new IllegalArgumentException("Read timeout must be positive");
        }
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
     * @throws IllegalStateException if required configuration is missing
     */
    public RestClient build() {
        if (baseUrl == null) {
            throw new IllegalStateException("Base URL must be set before building");
        }

        log.info("Building RestClient with baseUrl: {}, connectTimeout: {}ms, readTimeout: {}ms",
                baseUrl, connectTimeout.toMillis(), readTimeout.toMillis());

        // Create HTTP client factory with timeout configuration
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout((int) connectTimeout.toMillis());
        factory.setReadTimeout((int) readTimeout.toMillis());

        RestClient.Builder builder = RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(factory);

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
