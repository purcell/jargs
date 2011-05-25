package jargs.examples.gnu;

import jargs.gnu.CmdLineParser;

public class OptionParserSubclassTest {

    private static class MyOptionsParser extends CmdLineParser {

        public static final Option<Boolean> VERBOSE = new
            Option<Boolean>('v',"verbose", false, CmdLineParser.flagParser, 
                "Print extra information");

        public static final Option<Integer> SIZE = new
            CmdLineParser.Option<Integer>('s',"size", true, CmdLineParser.integerParser,
                "The extent of the thing");

        public static final Option<String> NAME = new
            CmdLineParser.Option<String>('n',"name", true, CmdLineParser.stringParser,
                "Name given to the widget");

        public static final Option<Double> FRACTION = new
            CmdLineParser.Option<Double> ('f',"fraction", true, CmdLineParser.doubleParser,
                "What percentage should be discarded");

        public MyOptionsParser() {
            super();
            addOption(VERBOSE);
            addOption(SIZE);
            addOption(NAME);
            addOption(FRACTION);
        }
    }


    public static void main( String[] args ) {
        MyOptionsParser myOptions = new MyOptionsParser();

        try {
            myOptions.parse(args);
        }
        catch ( CmdLineParser.UnknownOptionException e ) {
            System.err.println(e.getMessage());
            myOptions.printUsage();
            System.exit(2);
        }
        catch ( CmdLineParser.IllegalOptionValueException e ) {
            System.err.println(e.getMessage());
            myOptions.printUsage();
            System.exit(2);
        }

        CmdLineParser.Option<?>[] allOptions =
            new CmdLineParser.Option[] { MyOptionsParser.VERBOSE,
                                         MyOptionsParser.NAME,
                                         MyOptionsParser.SIZE,
                                         MyOptionsParser.FRACTION };

        for ( int j = 0; j<allOptions.length; ++j ) {
            System.out.println(allOptions[j].longForm() + ": " +
                               allOptions[j].getValue ());
        }

        String[] otherArgs = myOptions.getRemainingArgs();
        System.out.println("remaining args: ");
        for ( int i = 0; i<otherArgs.length; ++i ) {
            System.out.println(otherArgs[i]);
        }
        System.exit(0);
    }

}
