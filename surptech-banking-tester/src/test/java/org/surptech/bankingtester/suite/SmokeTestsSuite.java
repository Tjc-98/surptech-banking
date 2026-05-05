package org.surptech.bankingtester.suite;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

/**
 * Test suite for running smoke tests only
 */
@Suite
@SuiteDisplayName("SurpTech Banking System - Smoke Tests")
@SelectPackages("org.surptech.bankingtester.tests")
@IncludeTags("smoke")
public class SmokeTestsSuite {
    // This class remains empty, it is used only as a holder for the above annotations
}
