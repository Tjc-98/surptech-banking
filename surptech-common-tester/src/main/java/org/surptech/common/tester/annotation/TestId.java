package org.surptech.common.tester.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation to mark test methods with unique test IDs.
 * This helps with test tracking, reporting, and traceability.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TestId {
    /**
     * The unique identifier for the test
     * @return test ID string
     */
    String value();
}
