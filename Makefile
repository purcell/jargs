# Settings that can be altered
JAVAC=javac

# Settings that should not be changed
ROOT=$(PWD)
LIB=$(ROOT)/lib
SRC=$(ROOT)/src
CLASSES=$(ROOT)/classes
JAR=$(LIB)/jargs.jar
TESTSRC=$(ROOT)/tests
TESTCLASSES=$(ROOT)/test-classes
TESTJAR=$(LIB)/jargs-tests.jar
JDOCDIR=$(ROOT)/doc/api
DOCTITLE="JArgs command line option parsing library"
BLURBHTML='For updates and more see <a target="_top" href="http://jargs.sourceforge.net/">jargs.sourceforge.net</a>'
COPYRIGHTHTML='Copyright &copy; 2001 Steve Purcell. Released under the terms of the BSD licence'
JAVA_TO_CLASS=

all: $(JAR) $(TESTJAR)


test-classes:
	mkdir -p $(TESTCLASSES)
	cd $(TESTSRC) && (find . -name '*java'|xargs $(JAVAC) -d $(TESTCLASSES) -classpath $(CLASSES))

classes:
	mkdir -p classes
	cd $(SRC) && (find . -name '*java'|xargs $(JAVAC) -d $(CLASSES))

$(TESTJAR): test-classes
	cd test-classes && jar cvf $(TESTJAR) .
$(JAR): classes
	cd classes && jar cvf $(JAR) .

jdoc: $(JAR)
	mkdir -p $(JDOCDIR)
	cd src && (find . -name '*java' | xargs \
		javadoc -author -version -d $(JDOCDIR) -sourcepath . \
		-classpath $(JAR) \
		-windowtitle $(DOCTITLE) -doctitle $(DOCTITLE) \
		-header $(BLURBHTML) -footer $(BLURBHTML) \
		-bottom $(COPYRIGHTHTML))

clean:
	rm -rf $(CLASSES) $(TESTCLASSES) $(JAR) $(TESTJAR) $(JDOCDIR)

.PHONY: all classes test-classes
