package org.surptech.dataaggregator.procedure;

import org.surptech.dataaggregator.config.ApplicationContextProvider;
import org.surptech.dataaggregator.service.ApplicationServices;

public abstract class BaseProcedure<REQUEST, RESPONSE> {

    protected REQUEST request;
    protected RESPONSE response;
    protected ApplicationServices applicationServices;

    public BaseProcedure(REQUEST request) {
        this.request = request;
        this.applicationServices = ApplicationContextProvider.getApplicationContext()
                .getBean(ApplicationServices.class);
    }

    public abstract RESPONSE executeProcedure();

    public REQUEST getRequest() {
        return request;
    }

    public RESPONSE getResponse() {
        return response;
    }
}
