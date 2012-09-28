/**
 * Copyright (c) 2001-2012 Steve Purcell.
 * Copyright (c) 2002      Vidar Holen.
 * Copyright (c) 2002      Michal Ceresna.
 * Copyright (c) 2005      Ewan Mellor.
 * Copyright (c) 2010-2012 penSec.IT UG (haftungsbeschränkt).
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. Neither the name of the copyright holder nor the names
 * of its contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package com.sanityinc.jargs;

import com.sanityinc.jargs.CmdLineParser.Option;

import java.util.Collection;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.junit.Test;

public class CmdLineParserTestCase {

    @Test
    public void testStandardOptions() throws Exception {
        CmdLineParser parser = new CmdLineParser();
        Option<Boolean> verbose = parser.addBooleanOption('v', "verbose");
        Option<Integer> size = parser.addIntegerOption('s', "size");
        Option<String> name = parser.addStringOption('n', "name");
        Option<Double> fraction = parser.addDoubleOption('f', "fraction");
        Option<Boolean> missing = parser.addBooleanOption('m', "missing");
        Option<Boolean> careful = parser.addBooleanOption("careful");
        Option<Long> bignum = parser.addLongOption('b', "bignum");
        assertEquals(null, parser.getOptionValue(size));
        Long longValue = new Long(new Long(Integer.MAX_VALUE).longValue() + 1);
        parser.parse(new String[] { "-v", "--size=100", "-b",
                longValue.toString(), "-n", "foo", "-f", "0.1", "rest" },
                Locale.US);
        assertEquals(null, parser.getOptionValue(missing));
        assertEquals(Boolean.TRUE, parser.getOptionValue(verbose));
        assertEquals(100, parser.getOptionValue(size).intValue());
        assertEquals("foo", parser.getOptionValue(name));
        assertEquals(longValue, parser.getOptionValue(bignum));
        assertEquals(0.1, parser.getOptionValue(fraction).doubleValue(), 0.1e-6);
        assertArrayEquals(new String[]{"rest"}, parser.getRemainingArgs());
    }


    @Test
    public void testDefaults() throws Exception {
        CmdLineParser parser = new CmdLineParser();
        Option<Boolean> boolean1 = parser.addBooleanOption("boolean1");
        Option<Boolean> boolean2 = parser.addBooleanOption("boolean2");
        Option<Boolean> boolean3 = parser.addBooleanOption("boolean3");
        Option<Boolean> boolean4 = parser.addBooleanOption("boolean4");
        Option<Boolean> boolean5 = parser.addBooleanOption("boolean5");

        Option<Integer> int1 = parser.addIntegerOption("int1");
        Option<Integer> int2 = parser.addIntegerOption("int2");
        Option<Integer> int3 = parser.addIntegerOption("int3");
        Option<Integer> int4 = parser.addIntegerOption("int4");

        Option<String> string1 = parser.addStringOption("string1");
        Option<String> string2 = parser.addStringOption("string2");
        Option<String> string3 = parser.addStringOption("string3");
        Option<String> string4 = parser.addStringOption("string4");

        parser.parse(new String[] {
          "--boolean1", "--boolean2",
          "--int1=42", "--int2=42",
          "--string1=Hello", "--string2=Hello",
        });

        assertEquals(Boolean.TRUE, parser.getOptionValue(boolean1));
        assertEquals(Boolean.TRUE,
                     parser.getOptionValue(boolean2, Boolean.FALSE));
        assertEquals(null, parser.getOptionValue(boolean3));
        assertEquals(Boolean.FALSE,
                     parser.getOptionValue(boolean4, Boolean.FALSE));
        assertEquals(Boolean.TRUE,
                     parser.getOptionValue(boolean5, Boolean.TRUE));

        Integer forty_two  = new Integer(42);
        Integer thirty_six = new Integer(36);

        assertEquals(forty_two,  parser.getOptionValue(int1));
        assertEquals(forty_two,  parser.getOptionValue(int2, thirty_six));
        assertEquals(null,       parser.getOptionValue(int3));
        assertEquals(thirty_six, parser.getOptionValue(int4, thirty_six));

        assertEquals("Hello",   parser.getOptionValue(string1));
        assertEquals("Hello",   parser.getOptionValue(string2, "Goodbye"));
        assertEquals(null,      parser.getOptionValue(string3));
        assertEquals("Goodbye", parser.getOptionValue(string4, "Goodbye"));
    }


    @Test
    public void testMultipleUses() throws Exception {
        CmdLineParser parser = new CmdLineParser();
        Option<Boolean> verbose = parser.addBooleanOption('v', "verbose");
        Option<Boolean> foo = parser.addBooleanOption('f', "foo");
        Option<Boolean> bar = parser.addBooleanOption('b', "bar");

        parser.parse(new String[] {
          "--foo", "-v", "-v", "--verbose", "-v", "-b", "rest"
        });

        int verbosity = 0;
        while (true) {
            Boolean b = parser.getOptionValue(verbose);

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


    @Test
    public void testCombinedFlags() throws Exception {
        CmdLineParser parser = new CmdLineParser();
        Option<Boolean> alt = parser.addBooleanOption('a', "alt");
        Option<Boolean> debug = parser.addBooleanOption('d', "debug");
        Option<Boolean> verbose = parser.addBooleanOption('v', "verbose");
        parser.parse(new String[] {
          "-dv"
        });

        assertEquals(null, parser.getOptionValue(alt));
        assertEquals(Boolean.TRUE, parser.getOptionValue(debug));
        assertEquals(Boolean.TRUE, parser.getOptionValue(verbose));
    }


    @Test
    public void testExplictlyTerminatedOptions() throws Exception {
        CmdLineParser parser = new CmdLineParser();
        Option<Boolean> alt = parser.addBooleanOption('a', "alt");
        Option<Boolean> debug = parser.addBooleanOption('d', "debug");
        Option<Boolean> verbose = parser.addBooleanOption('v', "verbose");
        Option<Double> fraction = parser.addDoubleOption('f', "fraction");
        parser.parse(new String[] {
          "-a", "hello", "-d", "-f", "10", "--", "goodbye", "-v", "welcome",
          "-f", "-10"
        });

        assertEquals(Boolean.TRUE,   parser.getOptionValue(alt));
        assertEquals(Boolean.TRUE,   parser.getOptionValue(debug));
        assertEquals(null,           parser.getOptionValue(verbose));
        assertEquals(new Double(10), parser.getOptionValue(fraction));

        assertArrayEquals(
          new String[]{"hello", "goodbye", "-v", "welcome", "-f", "-10"},
          parser.getRemainingArgs());
    }


    @Test
    public void testGetOptionValues() throws Exception {
        CmdLineParser parser = new CmdLineParser();
        Option<Boolean> verbose = parser.addBooleanOption('v', "verbose");
        Option<Boolean> foo = parser.addBooleanOption('f', "foo");
        Option<Boolean> bar = parser.addBooleanOption('b', "bar");

        parser.parse(new String[] {
          "--foo", "-v", "-v", "--verbose", "-v", "-b", "rest"
        });

        int verbosity = 0;
        Collection<Boolean> v = parser.getOptionValues(verbose);
        for (Boolean b : v) {

            if (b == Boolean.TRUE) {
                verbosity++;
            } else {
                assertEquals(Boolean.FALSE, b);
                verbosity--;
            }
        }

        assertEquals(4, verbosity);
    }


    @Test
    public void testBadFormat() throws Exception {
        CmdLineParser parser = new CmdLineParser();
        Option<Integer> size = parser.addIntegerOption('s', "size");
        try {
            parser.parse(new String[] { "--size=blah" });
            fail("Expected IllegalOptionValueException");
        } catch (CmdLineParser.IllegalOptionValueException e) {
            // pass
        }
    }

    @Test
    public void testResetBetweenParse() throws Exception {
        CmdLineParser parser = new CmdLineParser();
        Option<Boolean> verbose = parser.addBooleanOption('v', "verbose");
        parser.parse(new String[] { "-v" });
        assertEquals(Boolean.TRUE, parser.getOptionValue(verbose));
        parser.parse(new String[] {});
        assertEquals(null, parser.getOptionValue(verbose));
    }

    @Test
    public void testLocale() throws Exception {
        CmdLineParser parser = new CmdLineParser();
        Option<Double> fraction = parser.addDoubleOption('f', "fraction");
        parser.parse(new String[] { "--fraction=0.2" }, Locale.US);
        assertEquals(0.2, parser.getOptionValue(fraction).doubleValue(), 0.1e-6);
        parser.parse(new String[] { "--fraction=0,2" }, Locale.GERMANY);
        assertEquals(0.2, parser.getOptionValue(fraction).doubleValue(), 0.1e-6);
    }

    @Test
    public void testDetachedOption() throws Exception {
        CmdLineParser parser = new CmdLineParser();
        Option<Boolean> detached = new CmdLineParser.Option.BooleanOption(
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

    @Test
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

    @Test
    public void testWhitespaceValueForStringOption() throws Exception {
        CmdLineParser parser = new CmdLineParser();
        Option<String> opt = parser.addStringOption('o', "option");
        parser.parse(new String[] {"-o", " "});
        assertEquals(" ", parser.getOptionValue(opt));
    }

    private void assertArrayEquals(Object[] expected, Object[] actual) {
        assertNotNull(actual);
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; ++i) {
            assertEquals(expected[i], actual[i]);
        }
    }

}

