package org.surptech.customerprofiletester.suite;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

/**
 * Smoke test suite - Quick validation of critical functionality
 */
@Suite
@SuiteDisplayName("Customer Profile - Smoke Tests")
@SelectPackages("org.surptech.customerprofiletester.tests")
@IncludeTags("smoke")
public class SmokeTestsSuite {
}
