JArgs command-line argument parsing library
===========================================

Copyright (c) 2001-2003 Steve Purcell.
Copyright (c) 2002      Vidar Holen.
Copyright (c) 2002      Michal Ceresna.
Copyright (c) 2005      Ewan Mellor.

All rights reserved.

Released under the terms of the BSD licence.  See the file LICENCE for
details.


Prerequisites
-------------

For each prerequisite, the version with which JArgs has been tested is given 
in parentheses.  Any version equal to or later than this should work.

Apache Ant (1.4.1), by The Apache Software Foundation, from
http://ant.apache.org/.  Ant is used to build JArgs, and to run its tests.

JUnit (3.7), by Eric Gamma, et al, from http://sourceforge.net/projects/junit.
JUnit is used to run the unit tests, and is not needed to run the library
itself.


Installation
------------

To compile, package, and test the code, run

ant

Two jars are created, one called lib/jargs.jar, which contains the runtime
library, and one called lib/jargs-test.jar, which contains the unit tests and
the examples.  The Javadoc APIs are created in doc/api.

To use the library with your own code, simply ensure that lib/jargs.jar is on
the CLASSPATH.


Documentation
-------------

The main documentation is the detailed worked example in
src/jargs/examples/gnu/OptionTest.java, plus the generated API documentation
in doc/api/.


Package contents
----------------

  src/jargs/gnu          -- The library itself.
  src/jargs/examples/gnu -- Examples showing how to use the library.
  src/jargs/test/gnu     -- JUnit tests.
  doc/                   -- API and other documentation.
  classes/               -- Compiled classes, once built.
  lib/                   -- JArgs jars, once built.
