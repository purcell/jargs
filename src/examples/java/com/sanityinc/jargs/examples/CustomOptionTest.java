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

package com.sanityinc.jargs.examples;

import com.sanityinc.jargs.CmdLineParser;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Date;

public class CustomOptionTest {

    private static void printUsage() {
        System.err.println("usage: prog [{-d,--date} date]");
    }


    /**
     * A custom type of command line option corresponding to a short
     * date value, e.g. .
     */
    public static class ShortDateOption extends CmdLineParser.Option<Date> {

        public ShortDateOption( char shortForm, String longForm ) {
            super(shortForm, longForm, true);
        }

        @Override
        protected Date parseValue( String arg, Locale locale )
            throws CmdLineParser.IllegalOptionValueException {
            try {
                DateFormat dateFormat =
                    DateFormat.getDateInstance(DateFormat.SHORT, locale);
                return dateFormat.parse(arg);
            } catch (ParseException e) {
                throw new CmdLineParser.IllegalOptionValueException(this, arg);
            }
        }
    }

    public static void main( String[] args ) {
        CmdLineParser parser = new CmdLineParser();
        CmdLineParser.Option<Date> date =
            parser.addOption(new ShortDateOption('d', "date"));

        try {
            parser.parse(args);
        } catch ( CmdLineParser.OptionException e ) {
            System.err.println(e.getMessage());
            printUsage();
            System.exit(2);
        }

        // Extract the values entered for the various options -- if the
        // options were not specified, the corresponding values will be
        // null.
        Date dateValue = parser.getOptionValue(date);

        // For testing purposes, we just print out the option values
        System.out.println("date: " + dateValue);

        // Extract the trailing command-line arguments ('a_number') in the
        // usage string above.
        String[] otherArgs = parser.getRemainingArgs();
        System.out.println("remaining args: ");
        for ( int i = 0; i < otherArgs.length; ++i ) {
            System.out.println(otherArgs[i]);
        }

        // In a real program, one would pass the option values and other
        // arguments to a function that does something more useful.

        System.exit(0);
    }

}

