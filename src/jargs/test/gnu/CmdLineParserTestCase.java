package jargs.test.gnu;

import jargs.gnu.CmdLineParser;
import java.util.Locale;
import junit.framework.TestCase;

public class CmdLineParserTestCase extends TestCase {


    public CmdLineParserTestCase(String name) {
        super(name);
    }

    public void testStandardOptions() throws Exception {
        CmdLineParser parser = new CmdLineParser();
        CmdLineParser.Option verbose = parser.addBooleanOption('v', "verbose");
        CmdLineParser.Option size = parser.addIntegerOption('s', "size");
        CmdLineParser.Option name = parser.addStringOption('n', "name");
        CmdLineParser.Option fraction = parser.addDoubleOption('f', "fraction");
        CmdLineParser.Option missing = parser.addBooleanOption('m', "missing");
        this.assertEquals(null, parser.getOptionValue(size));
        parser.parse(new String[]{"-v", "--size=100", "-n", "foo", "-f",
                                  "0.1", "rest"}, Locale.US);
        this.assertEquals(null, parser.getOptionValue(missing));
        this.assertEquals(Boolean.TRUE, parser.getOptionValue(verbose));
        this.assertEquals(100, ((Integer)parser.getOptionValue(size)).intValue());
        this.assertEquals("foo", parser.getOptionValue(name));
        this.assertEquals(0.1, ((Double)parser.getOptionValue(fraction)).doubleValue(), 0.1e-6);
        String[] otherArgs = parser.getRemainingArgs();
        assertEquals(1, otherArgs.length);
        assertEquals("rest", otherArgs[0]);
    }

    public void testBadFormat() throws Exception {
        CmdLineParser parser = new CmdLineParser();
        CmdLineParser.Option size = parser.addIntegerOption('s', "size");
        try {
            parser.parse(new String[]{"--size=blah"});
            fail("Expected IllegalOptionValueException");
        }
        catch (CmdLineParser.IllegalOptionValueException e) {
            // pass
        }
    }

    public void testResetBetweenParse() throws Exception {
        CmdLineParser parser = new CmdLineParser();
        CmdLineParser.Option verbose = parser.addBooleanOption('v', "verbose");
        parser.parse(new String[]{"-v"});
        this.assertEquals(Boolean.TRUE, parser.getOptionValue(verbose));
        parser.parse(new String[]{});
        this.assertEquals(null, parser.getOptionValue(verbose));
    }

    public void testLocale() throws Exception {
        CmdLineParser parser = new CmdLineParser();
        CmdLineParser.Option fraction = parser.addDoubleOption('f', "fraction");
        parser.parse(new String[]{"--fraction=0.2"}, Locale.US);
        this.assertEquals(0.2, ((Double)parser.getOptionValue(fraction)).doubleValue(), 0.1e-6);
        parser.parse(new String[]{"--fraction=0,2"}, Locale.GERMANY);
        this.assertEquals(0.2, ((Double)parser.getOptionValue(fraction)).doubleValue(), 0.1e-6);
    }

    public void testDetachedOption() throws Exception {
        CmdLineParser parser = new CmdLineParser();
        CmdLineParser.Option detached = new CmdLineParser.Option.BooleanOption('v', "verbose");
        this.assertEquals(null, parser.getOptionValue(detached));
        try {
            parser.parse(new String[] {"-v"});
            this.fail("UnknownOptionException expected");
        }
        catch (CmdLineParser.UnknownOptionException e) {
            // pass
        }
        this.assertEquals(null, parser.getOptionValue(detached));
    }
}
