package org.surptech.customerprofile.procedure;

import org.surptech.customerprofile.service.ApplicationServices;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Getter
@Service
public abstract class BaseProcedure<RT, RS> {
    static ApplicationServices applicationServices;

    RT request;
    RS response;

    BaseProcedure() {}

    BaseProcedure(RT request) {
        this.request = request;
    }

    public static synchronized void setApplicationServices(ApplicationServices applicationServicesInstance) {
        applicationServices = applicationServicesInstance;
    }

    public abstract RS executeProcedure();
}
