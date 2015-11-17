#
# A simple makefile for compiling three java classes
#

# define a makefile variable for the java compiler
#
JCC = javac

# define a makefile variable for compilation flags
# the -g flag compiles with debugging information
#
JFLAGS = -g
JARFLAGS = -classpath "../deuceAgent-1.3.0.jar:."

# typing 'make' will invoke the first target entry in the makefile 
# (the default one in this case)
#
default: concurrent_fail_retrieve.class hopscotch.class MyThread.class

concurrent_fail_retrieve.class: concurrent_fail_retrieve.java
	$(JCC) $(JFLAGS) $(JARFLAGS) concurrent_fail_retrieve.java

hopscotch.class: hopscotch.java
	$(JCC) $(JFLAGS) hopscotch.java

MyThread.class: MyThread.java
	$(JCC) $(JFLAGS) MyThread.java


# To start over from scratch, type 'make clean'.  
# Removes all .class files, so that the next make rebuilds them
#
clean: 
	$(RM) *.class
	
#run:
#	java -javaagent:"deuceAgent-1.3.0.jar" concurrent_fail_retrieve 5