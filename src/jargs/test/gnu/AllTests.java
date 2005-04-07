package jargs.test.gnu;

import junit.framework.TestSuite;

public class AllTests {
    public static TestSuite suite() {
        TestSuite s = new TestSuite();
        s.addTestSuite(CmdLineParserTestCase.class);
        s.addTestSuite(CustomOptionTestCase.class);
        return s;
    }
}
