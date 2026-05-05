package org.surptech.customerprofile.controller;

import org.surptech.customerprofile.procedure.BaseProcedure;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Log4j2
public class BaseController {

    public <RT, RS> RS runProcedure(BaseProcedure<RT, RS> procedure) {
        HttpServletRequest httpServletRequest =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String path = httpServletRequest.getServletPath();

        log.info("Incoming Traffic for path {}", path);
        log.info(procedure.getRequest());

        procedure.executeProcedure();

        log.info(procedure.getResponse());

        log.info("End Traffic for path {}", path);

        return procedure.getResponse();
    }
}
