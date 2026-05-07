package org.surptech.dataaggregator.procedure;

import org.surptech.dataaggregator.config.ApplicationContextProvider;
import org.surptech.dataaggregator.service.ApplicationServices;

public abstract class BaseProcedure<REQUEST, RESPONSE> {

    protected final REQUEST request;
    protected final ApplicationServices applicationServices;

    public BaseProcedure(REQUEST request) {
        this.request = request;
        this.applicationServices = ApplicationContextProvider.getApplicationContext()
                .getBean(ApplicationServices.class);
    }

    public abstract RESPONSE executeProcedure();

    public REQUEST getRequest() {
        return request;
    }
}
