package jargs.examples.gnu;

import jargs.gnu.CmdLineParser;
import jargs.gnu.CmdLineParser.IllegalOptionValueException;
import jargs.gnu.CmdLineParser.OptionValueParser;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Date;

public class CustomOptionTest {

    

    /**
     * A custom type of command line option corresponding to a short
     * date value, e.g. .
     */
    private static OptionValueParser<Date> shortDateParser = new OptionValueParser<Date>() {

      @Override
      public Date parse (String arg, Locale locale) throws IllegalOptionValueException {
        try {
          DateFormat dateFormat =
              DateFormat.getDateInstance(DateFormat.SHORT, locale);
          return dateFormat.parse(arg);
        }
        catch (ParseException e) {
          throw new CmdLineParser.IllegalOptionValueException(this, arg);
        }
      }
    };
    

    public static void main( String[] args ) {
        CmdLineParser parser = new CmdLineParser();
        CmdLineParser.Option<Date> date =
          parser.addUserDefinedOption ('d', "date", shortDateParser, "enter date");

        try {
            parser.parse(args);
        }
        catch ( CmdLineParser.OptionException e ) {
            System.err.println(e.getMessage());
            parser.printUsage();
            System.exit(2);
        }

        // Extract the values entered for the various options -- if the
        // options were not specified, the corresponding values will be
        // null.
        Date dateValue = date.getValue ();

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
