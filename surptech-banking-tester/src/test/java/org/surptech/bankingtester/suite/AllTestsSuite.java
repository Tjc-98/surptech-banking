package org.surptech.bankingtester.suite;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

/**
 * Test suite for running all tests
 */
@Suite
@SuiteDisplayName("SurpTech Banking System - All Tests")
@SelectPackages("org.surptech.bankingtester.tests")
public class AllTestsSuite {
    // This class remains empty, it is used only as a holder for the above annotations
}
