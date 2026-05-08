package org.surptech.common.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Interceptor for logging REST client requests and responses.
 * Logs method, URI, headers, status code, and execution time.
 */
@Slf4j
public class RestClientLoggingInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {
        
        long startTime = System.currentTimeMillis();
        
        logRequest(request, body);
        
        ClientHttpResponse response = null;
        try {
            response = execution.execute(request, body);
            logResponse(response, startTime);
            return response;
        } catch (IOException e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("REST Client Request Failed - Method: {}, URI: {}, Duration: {}ms, Error: {}",
                    request.getMethod(), request.getURI(), duration, e.getMessage());
            throw e;
        }
    }

    private void logRequest(HttpRequest request, byte[] body) {
        if (log.isDebugEnabled()) {
            log.debug("REST Client Request - Method: {}, URI: {}, Headers: {}",
                    request.getMethod(), request.getURI(), request.getHeaders());
            
            if (body.length > 0) {
                String bodyString = new String(body, StandardCharsets.UTF_8);
                log.debug("REST Client Request Body: {}", bodyString);
            }
        } else {
            log.info("REST Client Request - Method: {}, URI: {}",
                    request.getMethod(), request.getURI());
        }
    }

    private void logResponse(ClientHttpResponse response, long startTime) throws IOException {
        long duration = System.currentTimeMillis() - startTime;
        
        if (log.isDebugEnabled()) {
            log.debug("REST Client Response - Status: {}, Duration: {}ms, Headers: {}",
                    response.getStatusCode(), duration, response.getHeaders());
        } else {
            log.info("REST Client Response - Status: {}, Duration: {}ms",
                    response.getStatusCode(), duration);
        }
    }
}
