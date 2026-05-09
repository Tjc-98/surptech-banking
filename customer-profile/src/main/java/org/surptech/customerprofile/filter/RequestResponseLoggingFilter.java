package org.surptech.customerprofile.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Filter to log incoming requests and outgoing responses.
 * This filter captures and logs the body, headers, and performance metrics for all HTTP requests/responses.
 */
@Slf4j
@Component
public class RequestResponseLoggingFilter implements Filter {

    private static final String REQUEST_LOG_SEPARATOR = "========== Incoming Request ==========";
    private static final String RESPONSE_LOG_SEPARATOR = "========== Outgoing Response ==========";
    private static final String END_SEPARATOR = "=======================================";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            log.warn("Received non-HTTP request/response, skipping logging");
            chain.doFilter(request, response);
            return;
        }

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Wrap request and response to cache content
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(httpRequest, 0);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(httpResponse);

        long startTime = System.currentTimeMillis();

        try {
            // Log incoming request
            logIncomingRequest(requestWrapper);

            // Continue with the filter chain
            chain.doFilter(requestWrapper, responseWrapper);
            
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            
            // Log outgoing response
            logOutgoingResponse(responseWrapper, duration);

            // Copy response content back to original response
            responseWrapper.copyBodyToResponse();
        }
    }

    /**
     * Logs the incoming HTTP request with its method, URI, headers, and body.
     *
     * @param request the HTTP request to log
     */
    private void logIncomingRequest(ContentCachingRequestWrapper request) {
        if (!log.isInfoEnabled()) {
            return; // Skip processing if info level logging is disabled
        }

        StringBuilder requestLog = new StringBuilder()
            .append("\n").append(REQUEST_LOG_SEPARATOR).append("\n")
            .append("Method: ").append(request.getMethod()).append("\n")
            .append("URI: ").append(request.getRequestURI());

        if (request.getQueryString() != null) {
            requestLog.append("?").append(request.getQueryString());
        }
        requestLog.append("\n");
        
        // Log headers
        requestLog.append("Headers:\n");
        request.getHeaderNames().asIterator().forEachRemaining(headerName -> {
            String headerValue = request.getHeader(headerName);
            requestLog.append("  ").append(headerName).append(": ").append(headerValue).append("\n");
        });

        // Log request body
        appendBody(requestLog, request.getContentAsByteArray(), "Body");
        requestLog.append(END_SEPARATOR);

        log.info(requestLog.toString());
    }

    /**
     * Logs the outgoing HTTP response with its status, duration, headers, and body.
     *
     * @param response the HTTP response to log
     * @param duration the request processing duration in milliseconds
     */
    private void logOutgoingResponse(ContentCachingResponseWrapper response, long duration) {
        if (!log.isInfoEnabled()) {
            return; // Skip processing if info level logging is disabled
        }

        StringBuilder responseLog = new StringBuilder()
            .append("\n").append(RESPONSE_LOG_SEPARATOR).append("\n")
            .append("Status: ").append(response.getStatus()).append("\n")
            .append("Duration: ").append(duration).append(" ms\n");

        // Log response headers
        responseLog.append("Headers:\n");
        response.getHeaderNames().forEach(headerName -> {
            String headerValue = response.getHeader(headerName);
            responseLog.append("  ").append(headerName).append(": ").append(headerValue).append("\n");
        });
        
        // Log response body
        appendBody(responseLog, response.getContentAsByteArray(), "Body");
        responseLog.append(END_SEPARATOR);

        log.info(responseLog.toString());
    }

    /**
     * Appends the request/response body to the log if it exists.
     *
     * @param logBuilder the StringBuilder to append to
     * @param contentBytes the content bytes to append
     * @param label the label for the body (e.g., "Body")
     */
    private void appendBody(StringBuilder logBuilder, byte[] contentBytes, String label) {
        if (contentBytes != null && contentBytes.length > 0) {
            String content = new String(contentBytes, StandardCharsets.UTF_8);
            logBuilder.append(label).append(": ").append(content).append("\n");
        }
    }
}
