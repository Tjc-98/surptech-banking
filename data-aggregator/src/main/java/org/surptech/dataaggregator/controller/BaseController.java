package org.surptech.dataaggregator.controller;

import org.surptech.dataaggregator.procedure.BaseProcedure;

public abstract class BaseController {

    protected <REQUEST, RESPONSE> RESPONSE runProcedure(BaseProcedure<REQUEST, RESPONSE> procedure) {
        return procedure.executeProcedure();
    }
}
