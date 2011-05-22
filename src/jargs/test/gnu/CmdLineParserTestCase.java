package jargs.test.gnu;

import jargs.gnu.CmdLineParser;

import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import junit.framework.TestCase;

public class CmdLineParserTestCase extends TestCase {

	public CmdLineParserTestCase(String name) {
		super(name);
	}

	public void testStandardOptions() throws Exception {
		CmdLineParser parser = new CmdLineParser();
		CmdLineParser.Option<Boolean> verbose = parser.addBooleanOption('v', "verbose", "enable verbose");
		CmdLineParser.Option<Integer> size = parser.addIntegerOption('s', "size", "enter size");
		CmdLineParser.Option<String> name = parser.addStringOption('n', "name", "enter name");
		CmdLineParser.Option<Double> fraction = parser.addDoubleOption('f', "fraction", "enter fraction");
		CmdLineParser.Option<Boolean> missing = parser.addBooleanOption('m', "missing", "enable missing");
		CmdLineParser.Option<Boolean> careful = parser.addBooleanOption("careful", "enable careful");
		CmdLineParser.Option<Long> bignum = parser.addLongOption('b', "bignum", "enter bignum");
		assertEquals(null, size.getValue ());
		Long longValue = new Long(new Long(Integer.MAX_VALUE).longValue() + 1);
		parser.parse(new String[] { "-v", "--size=100", "-b",
				longValue.toString(), "-n", "foo", "-f", "0.1", "rest" },
				Locale.US);
		assertEquals(null, missing.getValue ());
		assertEquals(Boolean.TRUE, verbose.getValue ());
		assertEquals(100, size.getValue ().intValue());
		assertEquals("foo", name.getValue ());
		assertEquals(longValue, bignum.getValue ());
		assertEquals(0.1, fraction.getValue (), 0.1e-6);
		assertArrayEquals(new String[]{"rest"}, parser.getRemainingArgs());
	}


    public void testDefaults() throws Exception {
		CmdLineParser parser = new CmdLineParser();
		CmdLineParser.Option<Boolean> boolean1 = parser.addBooleanOption("boolean1", "enable boolean1");
		CmdLineParser.Option<Boolean> boolean2 = parser.addBooleanOption("boolean2", "enable boolean2");
		CmdLineParser.Option<Boolean> boolean3 = parser.addBooleanOption("boolean3", "enable boolean3");
		CmdLineParser.Option<Boolean> boolean4 = parser.addBooleanOption("boolean4", "enable boolean4");
		CmdLineParser.Option<Boolean> boolean5 = parser.addBooleanOption("boolean5", "enable boolean5");

		CmdLineParser.Option<Integer> int1 = parser.addIntegerOption("int1", "enter int1");
		CmdLineParser.Option<Integer> int2 = parser.addIntegerOption("int2", "enter int2");
		CmdLineParser.Option<Integer> int3 = parser.addIntegerOption("int3", "enter int3");
		CmdLineParser.Option<Integer> int4 = parser.addIntegerOption("int4", "enter int4");

		CmdLineParser.Option<String> string1 = parser.addStringOption("string1", "enter string1");
		CmdLineParser.Option<String> string2 = parser.addStringOption("string2", "enter string2");
		CmdLineParser.Option<String> string3 = parser.addStringOption("string3", "enter string3");
		CmdLineParser.Option<String> string4 = parser.addStringOption("string4", "enter string4");

		parser.parse(new String[] {
          "--boolean1", "--boolean2",
          "--int1=42", "--int2=42",
          "--string1=Hello", "--string2=Hello",
        });

		assertEquals(Boolean.TRUE, boolean1.getValue());
		assertEquals(Boolean.TRUE, boolean2.getValue(Boolean.FALSE));
		assertEquals(null, boolean3.getValue());
		assertEquals(Boolean.FALSE, boolean4.getValue(Boolean.FALSE));
		assertEquals(Boolean.TRUE, boolean5.getValue(Boolean.TRUE));

        Integer forty_two  = new Integer(42);
        Integer thirty_six = new Integer(36);

		assertEquals(forty_two,  int1.getValue());
		assertEquals(forty_two,  int2.getValue(thirty_six));
		assertEquals(null,       int3.getValue());
		assertEquals(thirty_six, int4.getValue(thirty_six));

		assertEquals("Hello",   string1.getValue());
		assertEquals("Hello",   string2.getValue("Goodbye"));
		assertEquals(null,      string3.getValue());
		assertEquals("Goodbye", string4.getValue("Goodbye"));
    }


	public void testMultipleUses() throws Exception {
		CmdLineParser parser = new CmdLineParser();
		CmdLineParser.Option<Boolean> verbose = parser.addBooleanOption('v', "verbose", "enable verbose");
		CmdLineParser.Option<Boolean> foo = parser.addBooleanOption('f', "foo", "enable foo");
		CmdLineParser.Option<Boolean> bar = parser.addBooleanOption('b', "bar", "enable bar");

		parser.parse(new String[] {
          "--foo", "-v", "-v", "--verbose", "-v", "-b", "rest"
        });

        int verbosity = 0;
        while (true) {
            Boolean b = verbose.getValue ();

            if (b == null) {
                break;
            }

            if (b == Boolean.TRUE) {
                verbosity++;
            }
            else {
                assertEquals(Boolean.FALSE, b);
                verbosity--;
            }
        }

        assertEquals(4, verbosity);
    }


	public void testCombinedFlags() throws Exception {
		CmdLineParser parser = new CmdLineParser();
		CmdLineParser.Option<Boolean> alt = parser.addBooleanOption('a', "alt", "enable alt");
		CmdLineParser.Option<Boolean> debug = parser.addBooleanOption('d', "debug", "enable debug");
		CmdLineParser.Option<Boolean> verbose = parser.addBooleanOption('v', "verbose", "enable verbose");
		parser.parse(new String[] {
          "-dv"
        });

        assertEquals(null, alt.getValue());
        assertEquals(Boolean.TRUE, debug.getValue());
        assertEquals(Boolean.TRUE, verbose.getValue());
    }


	public void testExplictlyTerminatedOptions() throws Exception {
		CmdLineParser parser = new CmdLineParser();
		CmdLineParser.Option<Boolean> alt = parser.addBooleanOption('a', "alt", "enable alt");
		CmdLineParser.Option<Boolean> debug = parser.addBooleanOption('d', "debug", "enable debug");
		CmdLineParser.Option<Boolean> verbose = parser.addBooleanOption('v', "verbose", "enable verbose");
		CmdLineParser.Option<Double> fraction = parser.addDoubleOption('f', "fraction", "enter fraction");
		parser.parse(new String[] {
          "-a", "hello", "-d", "-f", "10", "--", "goodbye", "-v", "welcome",
          "-f", "-10"
        });

        assertEquals(Boolean.TRUE,   alt.getValue());
        assertEquals(Boolean.TRUE,   debug.getValue());
        assertEquals(null,           verbose.getValue());
        assertEquals(new Double(10), fraction.getValue());

		assertArrayEquals(
          new String[]{"hello", "goodbye", "-v", "welcome", "-f", "-10"},
          parser.getRemainingArgs());
    }


	public void testGetOptionValues() throws Exception {
		CmdLineParser parser = new CmdLineParser();
		CmdLineParser.Option<Boolean> verbose = parser.addBooleanOption('v', "verbose", "enable verbose");
		CmdLineParser.Option<Boolean> foo = parser.addBooleanOption('f', "foo", "enable foo");
		CmdLineParser.Option<Boolean> bar = parser.addBooleanOption('b', "bar", "enable bar");

		parser.parse(new String[] {
          "--foo", "-v", "-v", "--verbose", "-v", "-b", "rest"
        });

        int verbosity = 0;
        List<Boolean> v = verbose.getValues ();
        
        for (Boolean b: v) { 

            if (b == Boolean.TRUE) {
                verbosity++;
            }
            else {
                assertEquals(Boolean.FALSE, b);
                verbosity--;
            }
        }

        assertEquals(4, verbosity);
    }


	public void testBadFormat() throws Exception {
		CmdLineParser parser = new CmdLineParser();
		CmdLineParser.Option<Integer> size = parser.addIntegerOption('s', "size", "enter size");
		try {
			parser.parse(new String[] { "--size=blah" });
			fail("Expected IllegalOptionValueException");
		} catch (CmdLineParser.IllegalOptionValueException e) {
			// pass
		}
	}

	public void testResetBetweenParse() throws Exception {
		CmdLineParser parser = new CmdLineParser();
		CmdLineParser.Option<Boolean> verbose = parser.addBooleanOption('v', "verbose", "enable verbose");
		parser.parse(new String[] { "-v" });
		assertEquals(Boolean.TRUE, verbose.getValue());
		parser.parse(new String[] {});
		assertEquals(null, verbose.getValue());
	}

	public void testLocale() throws Exception {
		CmdLineParser parser = new CmdLineParser();
		CmdLineParser.Option<Double> fraction = parser.addDoubleOption('f', "fraction", "enter fraction");
		parser.parse(new String[] { "--fraction=0.2" }, Locale.US);
		assertEquals(0.2,  fraction.getValue().doubleValue(), 0.1e-6);
		parser.parse(new String[] { "--fraction=0,2" }, Locale.GERMANY);
		assertEquals(0.2, fraction.getValue().doubleValue(), 0.1e-6);
	}

	public void testDetachedOption() throws Exception {
		CmdLineParser parser = new CmdLineParser();
		CmdLineParser.Option<Boolean> detached = new CmdLineParser.Option<Boolean>( 'v', "verbose", false, 
		    CmdLineParser.flagParser, "enable verbose");
		assertEquals(null, detached.getValue ());
		try {
			parser.parse(new String[] { "-v" });
			fail("UnknownOptionException expected");
		} catch (CmdLineParser.UnknownOptionException e) {
			// pass
		}
		assertEquals(null, detached.getValue ());
	}

	public void testMissingValueForStringOption() throws Exception {
		CmdLineParser parser = new CmdLineParser();
		parser.addBooleanOption('v', "verbose", "enable verbose");
		parser.addStringOption('c', "config", "enter config");

		try {
			parser.parse(new String[] {"-v", "-c"});
			fail();
		} catch (CmdLineParser.IllegalOptionValueException e) {
		}
	}
	
	private void assertArrayEquals(Object[] expected, Object[] actual) {
		assertNotNull(actual);
		assertEquals(expected.length, actual.length);
		for (int i = 0; i < expected.length; ++i) {
			assertEquals(expected[i], actual[i]);
		}
	}

}
