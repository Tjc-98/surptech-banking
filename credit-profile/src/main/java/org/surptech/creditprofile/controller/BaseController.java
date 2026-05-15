package org.surptech.creditprofile.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.surptech.common.procedure.BaseProcedure;

@Slf4j
public class BaseController {

    /**
     * Executes a procedure and logs the request and response.
     *
     * @param procedure the procedure to execute
     * @param <RequestType>  the type of request
     * @param <ResponseType> the type of response
     * @return the response from the procedure execution
     */
    public <RequestType, ResponseType> ResponseType executeProcedure(BaseProcedure<RequestType, ResponseType> procedure) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (requestAttributes == null) {
            log.warn("Request context attributes not available, executing procedure without request context");
            return procedure.execute();
        }

        HttpServletRequest httpServletRequest = requestAttributes.getRequest();
        String requestPath = httpServletRequest.getServletPath();

        log.info("Incoming request for path: {}", requestPath);

        long startTime = System.currentTimeMillis();
        ResponseType response = procedure.execute();
        long duration = System.currentTimeMillis() - startTime;

        log.info("Completed request for path: {} (duration: {} ms)", requestPath, duration);

        return response;
    }
}
