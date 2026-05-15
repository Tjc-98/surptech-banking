package org.surptech.customerprofiletester.suite;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

/**
 * All tests suite - Runs all available tests
 */
@Suite
@SuiteDisplayName("Customer Profile - All Tests")
@SelectPackages("org.surptech.customerprofiletester.tests")
public class AllTestsSuite {
}
