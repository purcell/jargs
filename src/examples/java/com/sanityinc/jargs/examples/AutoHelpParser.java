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

import java.util.ArrayList;
import java.util.List;

/**
 * This example shows how to dynamically create basic output for a --help option.
 */
public class AutoHelpParser extends CmdLineParser {

    List<String> optionHelpStrings = new ArrayList<String>();

    public <T> Option<T> addHelp(Option<T> option, String helpString) {
        optionHelpStrings.add(" -" + option.shortForm() + "/--" + option.longForm() + ": " + helpString);
        return option;
    }

    public void printUsage() {
        System.err.println("usage: prog [options]");
        for (String help : optionHelpStrings) {
            System.err.println(help);
        }
    }

    public static void main( String[] args ) {
        AutoHelpParser parser = new AutoHelpParser();
        CmdLineParser.Option<Boolean> verbose = parser.addHelp(
                parser.addBooleanOption('v', "verbose"),
                "Print extra information");
        CmdLineParser.Option<Integer> size = parser.addHelp(
                parser.addIntegerOption('s', "size"),
                "The extent of the thing");
        CmdLineParser.Option<String> name = parser.addHelp(
                parser.addStringOption('n', "name"),
                "Name given to the widget");
        CmdLineParser.Option<Double> fraction = parser.addHelp(
                parser.addDoubleOption('f', "fraction"),
                "What percentage should be discarded");
        CmdLineParser.Option<Boolean> help = parser.addHelp(
                parser.addBooleanOption('h', "help"),
                "Show this help message");

        try {
            parser.parse(args);
        } catch ( CmdLineParser.OptionException e ) {
            System.err.println(e.getMessage());
            parser.printUsage();
            System.exit(2);
        }

        if ( parser.getOptionValue(help) ) {
            parser.printUsage();
            System.exit(0);
        }

        // Extract the values entered for the various options -- if the
        // options were not specified, the corresponding values will be
        // null.
        Boolean verboseValue = parser.getOptionValue(verbose);
        Integer sizeValue = parser.getOptionValue(size);
        String nameValue = parser.getOptionValue(name);
        Double fractionValue = parser.getOptionValue(fraction);

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

