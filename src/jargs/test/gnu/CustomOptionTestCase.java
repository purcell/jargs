package jargs.test.gnu;

import jargs.gnu.CmdLineParser;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import junit.framework.TestCase;

public class CustomOptionTestCase extends TestCase {

    public CustomOptionTestCase(String name) {
        super(name);
    }

    public void testCustomOption() throws Exception {
        Calendar calendar = Calendar.getInstance();
        CmdLineParser parser = new CmdLineParser();
        CmdLineParser.Option date =
            parser.addOption(new ShortDateOption('d', "date"));

        parser.parse(new String[]{"-d", "11/03/2003"}, Locale.UK);
        Date d = (Date)parser.getOptionValue(date);
        calendar.setTime(d);
        assertEquals(11,             calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(Calendar.MARCH, calendar.get(Calendar.MONTH));
        assertEquals(2003,           calendar.get(Calendar.YEAR));

        parser.parse(new String[]{"-d", "11/03/2003"}, Locale.US);
        d = (Date)parser.getOptionValue(date);
        calendar.setTime(d);
        assertEquals(3,                 calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(Calendar.NOVEMBER, calendar.get(Calendar.MONTH));
        assertEquals(2003,              calendar.get(Calendar.YEAR));
    }

    public void testIllegalCustomOption() throws Exception {
        CmdLineParser parser = new CmdLineParser();
        CmdLineParser.Option date =
            parser.addOption(new ShortDateOption('d', "date"));
        try {
            parser.parse(new String[]{"-d", "foobar"}, Locale.US);
            fail("Expected IllegalOptionValueException");
        }
        catch (CmdLineParser.IllegalOptionValueException e) {
            //pass
        }
    }

    public static class ShortDateOption extends CmdLineParser.Option {
        public ShortDateOption( char shortForm, String longForm ) {
            super(shortForm, longForm, true);
        }
        protected Object parseValue( String arg, Locale locale )
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
