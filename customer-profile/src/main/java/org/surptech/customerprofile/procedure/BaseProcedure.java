package org.surptech.customerprofile.procedure;

import lombok.Getter;
import org.surptech.customerprofile.config.ApplicationContextProvider;
import org.surptech.customerprofile.service.ApplicationServices;

@Getter
public abstract class BaseProcedure<RequestType, ResponseType> {
    protected final ApplicationServices applicationServices;

    RequestType request;
    ResponseType response;

    protected BaseProcedure() {
        this.applicationServices = ApplicationContextProvider.getBean(ApplicationServices.class);
    }

    protected BaseProcedure(RequestType request) {
        this.applicationServices = ApplicationContextProvider.getBean(ApplicationServices.class);
        this.request = request;
    }

    public abstract ResponseType executeProcedure();
}
