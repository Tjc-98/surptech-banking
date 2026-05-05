package org.surptech.customerprofile.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.surptech.customerprofile.procedure.BaseProcedure;

@Log4j2
public class BaseController {

    public <RequestType, ResponseType> ResponseType runProcedure(BaseProcedure<RequestType, ResponseType> procedure) {
        HttpServletRequest httpServletRequest =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String requestPath = httpServletRequest.getServletPath();

        log.info("Incoming Traffic for path {}", requestPath);
        log.info(procedure.getRequest());

        procedure.executeProcedure();

        log.info(procedure.getResponse());

        log.info("End Traffic for path {}", requestPath);

        return procedure.getResponse();
    }
}
