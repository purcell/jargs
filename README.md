JArgs command-line argument parsing library
===========================================

- Copyright (c) 2001-2019 Steve Purcell.
- Copyright (c) 2002      Vidar Holen.
- Copyright (c) 2002      Michal Ceresna.
- Copyright (c) 2005      Ewan Mellor.
- Copyright (c) 2010-2012 penSec.IT UG (haftungsbeschränkt).

All rights reserved.

Released under the terms of the BSD licence.  See the file `LICENCE` for
details.


Prerequisites
-------------

For each prerequisite, the version with which JArgs has been tested is given
in parentheses.  Any version equal to or later than this should work.

To build JArgs and run its tests you need on of

- [Apache Ant](http://ant.apache.org/) (1.8.2), by The Apache Software
  Foundation
- [Apache Maven](http://maven.apache.org/) (3.0.4), by The Apache Software
  Foundation

Moreover [JUnit](http://www.junit.org/) (4.3.1), by Eric Gamma, is used to run
the unit tests, but is not needed to run the library itself.


Installation
------------

To compile, package, and test the code, run either

    ant

or

    mvn clean package source:jar javadoc:jar jar:test-jar

Two jars are created, one called `target/jargs-$VERSION$.jar`, which contains
the runtime library, and one called `target/jargs-$VERSION$-tests.jar`, which
contains the unit tests and the examples.  The Javadoc APIs are created in
`target/site/apidocs`.

To use the library with your own code, simply ensure that
`target/jargs-$VERSION$.jar` is on the CLASSPATH.

Pre-built packages are available via Sonatype's OSS Snapshots maven
repository. Ensure you have the oss-public repository enabled in your
`pom.xml`:

```xml
<repository>
  <id>sonatype-oss-public</id>
  <url>https://oss.sonatype.org/content/groups/public/</url>
  <releases>
    <enabled>true</enabled>
  </releases>
  <snapshots>
    <enabled>true</enabled>
  </snapshots>
</repository>
```

Then add JArgs to your `dependencies` list tag:

```xml
<dependency>
  <groupId>com.sanityinc</groupId>
  <artifactId>jargs</artifactId>
  <version>2.0-SNAPSHOT</version>
</dependency>
```

Documentation
-------------

The main documentation is the detailed worked example in
`src/examples/java/com/sanityinc/jargs/examples/OptionTest.java`, plus the
generated API documentation in `target/site/apidocs`.


Package contents
----------------

- `src/main/java/com/sanityinc/jargs` -- The library itself.
- `src/examples/java/com/sanityinc/jargs/examples` -- Examples showing how to
  use the library.
- `src/test/java/com/sanityinc/jargs` -- JUnit tests.
- `target/site/apidocs` -- API and other documentation.
- `target/classes` -- Compiled classes, once built.
- `target/` -- JArgs jars, once built.

<hr>

[💝 Support this project and my other Open Source work](https://www.patreon.com/sanityinc)

[💼 LinkedIn profile](https://uk.linkedin.com/in/stevepurcell)

[✍ sanityinc.com](http://www.sanityinc.com/)

[🐦 @sanityinc](https://twitter.com/sanityinc)
