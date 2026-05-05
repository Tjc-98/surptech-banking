package org.surptech.customerprofile.procedure;

import org.surptech.customerprofile.config.ApplicationContextProvider;
import org.surptech.customerprofile.service.ApplicationServices;
import lombok.Getter;

@Getter
public abstract class BaseProcedure<RT, RS> {
    protected final ApplicationServices applicationServices;

    RT request;
    RS response;

    protected BaseProcedure() {
        this.applicationServices = ApplicationContextProvider.getBean(ApplicationServices.class);
    }

    protected BaseProcedure(RT request) {
        this.applicationServices = ApplicationContextProvider.getBean(ApplicationServices.class);
        this.request = request;
    }

    public abstract RS executeProcedure();
}
