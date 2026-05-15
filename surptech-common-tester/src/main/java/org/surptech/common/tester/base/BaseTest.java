package org.surptech.common.tester.base;

import lombok.extern.slf4j.Slf4j;

/**
 * Base test class providing common test lifecycle management and logging.
 * All test classes should extend this class to inherit common functionality.
 */
@Slf4j
public abstract class BaseTest extends TestLifecycleManager {
    
    /**
     * Initialize test resources. Override this method in subclasses
     * to perform test suite initialization.
     */
    protected static void initializeTestResources() {
        log.info("=".repeat(80));
        log.info("TEST SUITE INITIALIZATION");
        log.info("=".repeat(80));
    }
}
