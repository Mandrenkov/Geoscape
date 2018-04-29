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
LIBPATH = lib/native
MAIN = core.Top
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
	@mkdir $(BUILDPATH)

	@echo -n "Compiling Java source files..."
	@javac -d $(BUILDPATH) -s $(BUILDPATH) -h $(BUILDPATH) -classpath $(CLASSPATH) $(SOURCES)
	@echo "done."

# Delete the build directory.
clean:
	@$(DELETE) $(BUILDPATH)