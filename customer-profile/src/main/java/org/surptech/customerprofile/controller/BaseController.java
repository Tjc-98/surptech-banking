package org.surptech.customerprofile.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.surptech.customerprofile.procedure.BaseProcedure;

@Slf4j
public class BaseController {

    public <RequestType, ResponseType> ResponseType executeProcedure(BaseProcedure<RequestType, ResponseType> procedure) {
        HttpServletRequest httpServletRequest =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String requestPath = httpServletRequest.getServletPath();

        log.info("Incoming request for path: {}", requestPath);
        log.info("Request data: {}", procedure.getRequest());

        ResponseType response = procedure.executeProcedure();

        log.info("Response data: {}", response);
        log.info("Completed request for path: {}", requestPath);

        return response;
    }
}
