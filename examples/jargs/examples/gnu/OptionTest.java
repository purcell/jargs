package jargs.examples.gnu;

import jargs.gnu.CmdLineParser;

public class OptionTest {

    private static void printUsage() {
        System.err.println("usage: prog [{-v,--verbose}] [{-n,--name} a_name]"+
                           "[{-s,--size} a_number]");
    }

    public static void main( String[] args ) {
        CmdLineParser parser = new CmdLineParser();
        CmdLineParser.Option verbose = parser.addBooleanOption('v', "verbose");
        CmdLineParser.Option size = parser.addIntegerOption('s', "size");
        CmdLineParser.Option name = parser.addStringOption('n', "name");

        try {
            parser.parse(args);
        }
        catch ( CmdLineParser.OptionException e ) {
            System.err.println(e.getMessage());
            printUsage();
            System.exit(2);
        }

        System.out.println("verbose: " + parser.getOptionValue(verbose));
        System.out.println("size: " + parser.getOptionValue(size));
        System.out.println("name: " + parser.getOptionValue(name));
        String[] otherArgs = parser.getRemainingArgs();
        System.out.println("remaining args: ");
        for ( int i = 0; i < otherArgs.length; ++i ) {
            System.out.println(otherArgs[i]);
        }
        System.exit(0);
    }

}
