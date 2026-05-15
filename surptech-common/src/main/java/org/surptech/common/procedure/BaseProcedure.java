package org.surptech.common.procedure;

import lombok.extern.slf4j.Slf4j;
import org.surptech.common.logging.LoggingUtils;

/**
 * Base class for implementing procedural business logic with template method pattern.
 * Provides consistent and automatic logging, error handling, and execution flow for all procedures.
 *
 * Subclasses should override {@link #executeProcedure()} to implement specific business logic.
 * The {@link #execute()} method handles logging entry/exit, performance monitoring, and exception handling.
 *
 * @param <REQUEST> the input request type for this procedure
 * @param <RESPONSE> the output response type for this procedure
 */
@Slf4j
public abstract class BaseProcedure<REQUEST, RESPONSE> {

    /** The request object containing input data for procedure execution */
    protected final REQUEST request;

    /**
     * Constructs a procedure with the provided request data.
     *
     * @param request the request input for this procedure
     */
    protected BaseProcedure(REQUEST request) {
        this.request = request;
    }

    /**
     * Executes the procedure with standard logging, performance monitoring, and error handling.
     * Logs method entry, exit, execution duration, and any exceptions that occur.
     *
     * @return the procedure response
     * @throws Exception if the procedure execution fails
     */
    public final RESPONSE execute() {
        String procedureName = this.getClass().getSimpleName();
        
        try {
            log.info("Executing procedure: {}", procedureName);
            LoggingUtils.logMethodEntry(procedureName, request);
            
            RESPONSE response = executeProcedure();
            
            LoggingUtils.logMethodExit(procedureName, response);
            log.info("Procedure {} completed successfully", procedureName);
            
            return response;
            
        } catch (Exception exception) {
            log.error("Procedure {} failed with error: {}", procedureName, exception.getMessage(), exception);
            throw exception;
        }
    }

    /**
     * Implements the actual procedure business logic.
     * Subclasses must override this method to provide specific implementation.
     *
     * @return the procedure response with execution results
     */
    protected abstract RESPONSE executeProcedure();
}
