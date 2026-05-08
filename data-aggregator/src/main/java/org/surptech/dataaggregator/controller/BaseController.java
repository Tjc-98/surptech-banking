package org.surptech.dataaggregator.controller;

import org.surptech.common.procedure.BaseProcedure;

public abstract class BaseController {

    protected <REQUEST, RESPONSE> RESPONSE executeProcedure(BaseProcedure<REQUEST, RESPONSE> procedure) {
        return procedure.execute();
    }
}
