package jargs.test.gnu;

import jargs.gnu.CmdLineParser;
import java.util.Locale;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import junit.framework.TestCase;

public class CustomOptionTestCase extends TestCase {

    public CustomOptionTestCase(String name) {
        super(name);
    }

    public void testCustomOption() throws Exception {
        CmdLineParser parser = new CmdLineParser();
        CmdLineParser.Option date =
            parser.addOption(new ShortDateOption('d', "date"));
        parser.parse(new String[]{"-d", "11/03/2003"}, Locale.UK);
        Date d = (Date)parser.getOptionValue(date);
        assertEquals(11, d.getDay());
        assertEquals(3, d.getMonth());
        assertEquals(2003, d.getYear());

        parser.parse(new String[]{"-d", "11/03/2003"}, Locale.US);
        d = (Date)parser.getOptionValue(date);
        assertEquals(3, d.getDay());
        assertEquals(11, d.getMonth());
        assertEquals(2003, d.getYear());
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
