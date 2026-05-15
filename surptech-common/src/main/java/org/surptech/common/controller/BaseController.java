package org.surptech.common.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.surptech.common.procedure.BaseProcedure;

/**
 * Base controller class providing common HTTP request handling functionality.
 * Provides access to HTTP request context and procedure execution logging.
 */
@Slf4j
public abstract class BaseController {

    /**
     * Executes a procedure with request context logging and performance metrics.
     * Measures request processing time and logs request path information.
     *
     * @param procedure the procedure to execute
     * @param <RequestType>  the request parameter type for the procedure
     * @param <ResponseType> the response return type for the procedure
     * @return the response from the procedure execution
     */
    protected <RequestType, ResponseType> ResponseType executeProcedure(BaseProcedure<RequestType, ResponseType> procedure) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (requestAttributes == null) {
            log.warn("Request context attributes not available, executing procedure without context");
            return procedure.execute();
        }

        HttpServletRequest httpRequest = requestAttributes.getRequest();
        String requestPath = httpRequest.getServletPath();

        log.info("Incoming request for path: {}", requestPath);

        long startTime = System.currentTimeMillis();
        ResponseType response = procedure.execute();
        long duration = System.currentTimeMillis() - startTime;

        log.info("Completed request for path: {} (duration: {} ms)", requestPath, duration);

        return response;
    }
}
