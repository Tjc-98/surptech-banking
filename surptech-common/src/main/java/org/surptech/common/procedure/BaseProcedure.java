package org.surptech.common.procedure;

import lombok.extern.slf4j.Slf4j;
import org.surptech.common.logging.LoggingUtils;

/**
 * Base class for all business logic procedures.
 * Provides template method pattern for consistent procedure execution with logging.
 *
 * @param <REQUEST> the request type
 * @param <RESPONSE> the response type
 */
@Slf4j
public abstract class BaseProcedure<REQUEST, RESPONSE> {

    protected final REQUEST request;

    protected BaseProcedure(REQUEST request) {
        this.request = request;
    }

    /**
     * Executes the procedure with standard logging and error handling.
     * Logs entry, exit, and any exceptions that occur.
     *
     * @return the procedure response
     * @throws Exception if the procedure fails
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
     * Implements the actual procedure logic.
     * Override this method in subclasses to provide specific business logic.
     *
     * @return the procedure response
     */
    protected abstract RESPONSE executeProcedure();
}
