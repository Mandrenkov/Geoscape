# Author:  Mikhail Andrenkov
# Date:    April 29, 2018
# Version: 1.1
#
# Description: Makefile for cleaning, building, and running the Geoscape application.
#              
#              Notes:
#                1. Only Windows and Linux distributions are supported.
#                2. Windows users must have Cygwin in their path.  Cygwin can be
#                   downloaded from: https://cygwin.com/install.html.

BUILDPATH = bin
DELETE = rm -rf
DOCPATH = doc
LIBPATH = lib/native
MAIN = core.Top
MKDIR = mkdir -p
SOURCES = $(wildcard */*.java)

ifeq ($(OS), Windows_NT)
	CLASSPATH = ".;$(BUILDPATH);lib/*"
else
	CLASSPATH = ".:$(BUILDPATH):lib/*"
endif

# Run the application.
all: run
run: build
	@echo "Running main class \"$(MAIN)\"."
	@java -classpath $(CLASSPATH) $(MAIN)

# Build the application.
build: clean
	@$(MKDIR) $(BUILDPATH)

	@echo -n "Compiling Java source files..."
	@javac -d $(BUILDPATH) -s $(BUILDPATH) -h $(BUILDPATH) -classpath $(CLASSPATH) $(SOURCES)
	@echo "done."

# Generate the documentation.
doc: FORCE
	@$(MKDIR) $(DOCPATH)

	@echo "Generating Javadoc documentation..."
	@javadoc -private -classpath $(CLASSPATH) -d doc/ $(SOURCES)
	@echo "The updated documentation is available in the $(DOCPATH)/ directory."

# Delete the build directory.
clean: FORCE
	@$(DELETE) $(BUILDPATH)

# Nonexistent file target.
FORCE: