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
		CmdLineParser.Option careful = parser.addBooleanOption("careful");
		CmdLineParser.Option bignum = parser.addLongOption('b', "bignum");
		assertEquals(null, parser.getOptionValue(size));
		Long longValue = new Long(new Long(Integer.MAX_VALUE).longValue() + 1);
		parser.parse(new String[] { "-v", "--size=100", "-b",
				longValue.toString(), "-n", "foo", "-f", "0.1", "rest" },
				Locale.US);
		assertEquals(null, parser.getOptionValue(missing));
		assertEquals(Boolean.TRUE, parser.getOptionValue(verbose));
		assertEquals(100, ((Integer) parser.getOptionValue(size)).intValue());
		assertEquals("foo", parser.getOptionValue(name));
		assertEquals(longValue, parser.getOptionValue(bignum));
		assertEquals(0.1, ((Double) parser.getOptionValue(fraction))
				.doubleValue(), 0.1e-6);
		assertArrayEquals(new String[]{"rest"}, parser.getRemainingArgs());
	}

	public void testBadFormat() throws Exception {
		CmdLineParser parser = new CmdLineParser();
		CmdLineParser.Option size = parser.addIntegerOption('s', "size");
		try {
			parser.parse(new String[] { "--size=blah" });
			fail("Expected IllegalOptionValueException");
		} catch (CmdLineParser.IllegalOptionValueException e) {
			// pass
		}
	}

	public void testResetBetweenParse() throws Exception {
		CmdLineParser parser = new CmdLineParser();
		CmdLineParser.Option verbose = parser.addBooleanOption('v', "verbose");
		parser.parse(new String[] { "-v" });
		assertEquals(Boolean.TRUE, parser.getOptionValue(verbose));
		parser.parse(new String[] {});
		assertEquals(null, parser.getOptionValue(verbose));
	}

	public void testLocale() throws Exception {
		CmdLineParser parser = new CmdLineParser();
		CmdLineParser.Option fraction = parser.addDoubleOption('f', "fraction");
		parser.parse(new String[] { "--fraction=0.2" }, Locale.US);
		assertEquals(0.2, ((Double) parser.getOptionValue(fraction))
				.doubleValue(), 0.1e-6);
		parser.parse(new String[] { "--fraction=0,2" }, Locale.GERMANY);
		assertEquals(0.2, ((Double) parser.getOptionValue(fraction))
				.doubleValue(), 0.1e-6);
	}

	public void testDetachedOption() throws Exception {
		CmdLineParser parser = new CmdLineParser();
		CmdLineParser.Option detached = new CmdLineParser.Option.BooleanOption(
				'v', "verbose");
		assertEquals(null, parser.getOptionValue(detached));
		try {
			parser.parse(new String[] { "-v" });
			fail("UnknownOptionException expected");
		} catch (CmdLineParser.UnknownOptionException e) {
			// pass
		}
		assertEquals(null, parser.getOptionValue(detached));
	}

	public void testMissingValueForStringOption() throws Exception {
		CmdLineParser parser = new CmdLineParser();
		parser.addBooleanOption('v', "verbose");
		parser.addStringOption('c', "config");

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