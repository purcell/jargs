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

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;

public class CustomOptionTestCase {

    @Test
    public void testCustomOption() throws Exception {
        Calendar calendar = Calendar.getInstance();
        CmdLineParser parser = new CmdLineParser();
        Option<Date> date =
            parser.addOption(new ShortDateOption('d', "date"));

        parser.parse(new String[]{"-d", "11/03/2003"}, Locale.UK);
        Date d = parser.getOptionValue(date);
        calendar.setTime(d);
        assertEquals(11,             calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(Calendar.MARCH, calendar.get(Calendar.MONTH));
        assertEquals(2003,           calendar.get(Calendar.YEAR));

        parser.parse(new String[]{"-d", "11/03/2003"}, Locale.US);
        d = parser.getOptionValue(date);
        calendar.setTime(d);
        assertEquals(3,                 calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(Calendar.NOVEMBER, calendar.get(Calendar.MONTH));
        assertEquals(2003,              calendar.get(Calendar.YEAR));
    }

    @Test
    public void testIllegalCustomOption() throws Exception {
        CmdLineParser parser = new CmdLineParser();
        Option<Date> date =
            parser.addOption(new ShortDateOption('d', "date"));
        try {
            parser.parse(new String[]{"-d", "foobar"}, Locale.US);
            fail("Expected IllegalOptionValueException");
        }
        catch (CmdLineParser.IllegalOptionValueException e) {
            //pass
        }
    }

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
            }
            catch (ParseException e) {
                throw new CmdLineParser.IllegalOptionValueException(this, arg);
            }
        }
    }

}

