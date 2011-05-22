package jargs.examples.gnu;

import jargs.gnu.CmdLineParser;

import java.util.List;


public class OptionTest {


    public static void main( String[] args ) {

        // First, you must create a CmdLineParser, and add to it the
        // appropriate Options.

        // To start with, we add the Options -d, -v, -s, and -f, with aliases
        // --debug, --verbose, --size, and --fraction respectively.

        // The -d and -v options have no associated value -- they are either
        // present, or they are not.  The -s and -f options take integer and
        // double-precision floating-point values respectively.

        CmdLineParser parser = new CmdLineParser();
        CmdLineParser.Option<Boolean> debug = parser.addBooleanOption ('d', "debug", "print debug messages");
        CmdLineParser.Option<Boolean> verbose = parser.addBooleanOption('v', "verbose", "print extra info");
        CmdLineParser.Option<Integer> size = parser.addIntegerOption('s', "size", "the size");
        CmdLineParser.Option<Double> fraction = parser.addDoubleOption('f', "fraction", "some fraction");

        // Options may have just a long form with no corresponding short form.
        // Here, we add --alt and --name options.

        CmdLineParser.Option<Boolean> alt = parser.addBooleanOption("alt", "alternate option for debug");
        CmdLineParser.Option<String> name = parser.addStringOption("name", "name of the thing");


        // Next, you must parse the user-provided command line arguments, and
        // catch any errors therein.

        // Options may appear on the command line in any order, and may even
        // appear after some or all of the non-option arguments.

        // If the user needs to specify non-option arguments that start with a
        // minus, then they may indicate the end of the parsable options with
        // -- , like this:

        // prog -f 20 -- -10 -fred

        // The -f 20 will be parsed as the fraction option, with the value 20.
        // The -10 and -fred arguments will be regarded as non-option
        // arguments, and passed through getRemainingArgs as unparsed Strings.

        // Short boolean options may be specified separately (-d -v) or
        // together (-dv).

        // Options with values may be given on the command line as -f 1.0 or
        // --fraction=1.0.

        try {
            parser.parse(args);
        }
        catch ( CmdLineParser.OptionException e ) {
            System.err.println(e.getMessage());
            parser.printUsage();
            System.exit(2);
        }


        // For options that may be specified only zero or one time, the value
        // of that option may be extracted as shown below.  If the options
        // were not specified, the corresponding values will be null.

        Boolean debugValue = debug.getValue ();
        String nameValue = name.getValue ();

        // Alternatively, you may specify a default value.  This will be
        // returned (instead of null) when the command line argument is
        // missing.

        Boolean altValue =
            alt.getValue(Boolean.FALSE);
        Integer sizeValue =
            size.getValue (new Integer(42));

        // If your application requires it, options may be specified more than
        // once.  In this case, you may get all the values specified by the
        // user, as a Vector:

        List<Double> fractionValues = fraction.getValues ();

        // Alternatively, you may make the loop explicit:

        int verbosity = 0;
        while (true) {
            Boolean verboseValue = verbose.getValue ();

            if (verboseValue == null) {
                break;
            }
            else {
                verbosity++;
            }
        }

        // The remaining command-line arguments -- those that do not start
        // with a minus sign -- can be captured like this:

        String[] otherArgs = parser.getRemainingArgs();


        // For testing purposes, we just print out the option values and
        // remaining command-line arguments.  In a real program, of course,
        // one would pass them to a function that does something more useful.

        System.out.println("debug: " + debugValue);
        System.out.println("alt: " + altValue);
        System.out.println("size: " + sizeValue);
        System.out.println("name: " + nameValue);

        System.out.println("verbosity: " + verbosity);

        for (Double f: fractionValues) {
            System.out.println("fraction: " + f);
        }

        System.out.println("remaining args: ");
        for ( int i = 0; i < otherArgs.length; ++i ) {
            System.out.println(otherArgs[i]);
        }

        System.exit(0);
    }
}
