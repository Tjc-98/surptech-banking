package org.surptech.dataaggregator.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

@Slf4j
@Component
public class RequestResponseLoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Wrap request and response to cache content
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(httpRequest, 0);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(httpResponse);

        long startTime = System.currentTimeMillis();

        try {
            // Log incoming request
            logRequest(requestWrapper);

            // Continue with the filter chain
            chain.doFilter(requestWrapper, responseWrapper);

        } finally {
            long duration = System.currentTimeMillis() - startTime;
            
            // Log outgoing response
            logResponse(responseWrapper, duration);

            // Copy response content back to original response
            responseWrapper.copyBodyToResponse();
        }
    }

    private void logRequest(ContentCachingRequestWrapper requestWrapper) {
        StringBuilder requestLog = new StringBuilder();
        requestLog.append("\n========== Incoming Request ==========\n");
        requestLog.append("Method: ").append(requestWrapper.getMethod()).append("\n");
        requestLog.append("URI: ").append(requestWrapper.getRequestURI());

        if (requestWrapper.getQueryString() != null) {
            requestLog.append("?").append(requestWrapper.getQueryString());
        }
        requestLog.append("\n");
        
        // Log headers
        requestLog.append("Headers:\n");
        Enumeration<String> headerNames = requestWrapper.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = requestWrapper.getHeader(headerName);
            requestLog.append("  ").append(headerName).append(": ").append(headerValue).append("\n");
        }
        
        // Log request body
        byte[] contentBytes = requestWrapper.getContentAsByteArray();
        if (contentBytes.length > 0) {
            String requestBody = new String(contentBytes, StandardCharsets.UTF_8);
            requestLog.append("Body: ").append(requestBody).append("\n");
        }
        
        requestLog.append("======================================");
        log.info(requestLog.toString());
    }

    private void logResponse(ContentCachingResponseWrapper responseWrapper, long duration) {
        StringBuilder responseLog = new StringBuilder();
        responseLog.append("\n========== Outgoing Response ==========\n");
        responseLog.append("Status: ").append(responseWrapper.getStatus()).append("\n");
        responseLog.append("Duration: ").append(duration).append(" ms\n");
        
        // Log response headers
        responseLog.append("Headers:\n");
        responseWrapper.getHeaderNames().forEach(headerName -> {
            String headerValue = responseWrapper.getHeader(headerName);
            responseLog.append("  ").append(headerName).append(": ").append(headerValue).append("\n");
        });
        
        // Log response body
        byte[] contentBytes = responseWrapper.getContentAsByteArray();
        if (contentBytes.length > 0) {
            String responseBody = new String(contentBytes, StandardCharsets.UTF_8);
            responseLog.append("Body: ").append(responseBody).append("\n");
        }
        
        responseLog.append("=======================================");
        log.info(responseLog.toString());
    }
}
