package jargs.examples.gnu;

import jargs.gnu.CmdLineParser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This example shows how to dynamically create basic output for a --help option. 
 */
public class AutoHelpParser extends CmdLineParser {
	List optionHelpStrings = new ArrayList();

	public Option addHelp(Option option, String helpString) {
		optionHelpStrings.add(" -" + option.shortForm() + "/--" + option.longForm() + ": " + helpString);
		return option;
	}
	
	public void printUsage() {
        System.err.println("usage: prog [options]");
        for (Iterator i = optionHelpStrings.iterator(); i.hasNext(); ) {
        	System.err.println(i.next());
        }
    }

    public static void main( String[] args ) {
    	AutoHelpParser parser = new AutoHelpParser();
    	CmdLineParser.Option verbose = parser.addHelp(
    			parser.addBooleanOption('v', "verbose"),
    			"Print extra information");
        CmdLineParser.Option size = parser.addHelp(
        		parser.addIntegerOption('s', "size"),
				"The extent of the thing");
        CmdLineParser.Option name = parser.addHelp(
        		parser.addStringOption('n', "name"),
				"Name given to the widget");
        CmdLineParser.Option fraction = parser.addHelp(
        		parser.addDoubleOption('f', "fraction"),
				"What percentage should be discarded");
        CmdLineParser.Option help = parser.addHelp(
        		parser.addBooleanOption('h', "help"),
				"Show this help message");

        try {
            parser.parse(args);
        }
        catch ( CmdLineParser.OptionException e ) {
            System.err.println(e.getMessage());
            parser.printUsage();
            System.exit(2);
        }

        if ( Boolean.TRUE.equals(parser.getOptionValue(help))) {
            parser.printUsage();
            System.exit(0);
        }

        // Extract the values entered for the various options -- if the
        // options were not specified, the corresponding values will be
        // null.
        Boolean verboseValue = (Boolean)parser.getOptionValue(verbose);
        Integer sizeValue = (Integer)parser.getOptionValue(size);
        String nameValue = (String)parser.getOptionValue(name);
        Double fractionValue = (Double)parser.getOptionValue(fraction);

        // For testing purposes, we just print out the option values
        System.out.println("verbose: " + verboseValue);
        System.out.println("size: " + sizeValue);
        System.out.println("name: " + nameValue);
        System.out.println("fraction: " + fractionValue);

        // Extract the trailing command-line arguments ('a_nother') in the
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
