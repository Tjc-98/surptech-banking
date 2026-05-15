package org.surptech.customerprofiletester.suite;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

/**
 * Integration test suite - Comprehensive integration testing
 */
@Suite
@SuiteDisplayName("Customer Profile - Integration Tests")
@SelectPackages("org.surptech.customerprofiletester.tests")
@IncludeTags("integration")
public class IntegrationTestsSuite {
}
