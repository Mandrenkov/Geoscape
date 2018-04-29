# Author:  Mikhail Andrenkov
# Date:    April 29, 2018
# Version: 1.1
#
# Description: Makefile for cleaning, building, and running the Geoscape application.
#              Currently, Windows and Linux are the only supported platforms.

BUILDPATH = bin
ifeq ($(OS), Windows_NT)
	CLASSPATH = ".;$(BUILDPATH);lib/*"
else
	CLASSPATH = ".:$(BUILDPATH):lib/*"
endif
LIBPATH = lib/native
MAIN = core.Top
DELETE = rm -rf
SOURCES = $(wildcard */*.java)

# Run the application.
all: run
run: build
	@echo "Running main class \"$(MAIN)\"."
	@java -classpath $(CLASSPATH) $(MAIN)

# Build the application.
build:
	@mkdir -p $(BUILDPATH)

	@echo -n "Compiling Java source files..."
	@javac -d $(BUILDPATH) -s $(BUILDPATH) -h $(BUILDPATH) -classpath $(CLASSPATH) $(SOURCES)
	@echo "done."

# Delete the build directory.
clean:
	@$(DELETE) $(BUILDPATH)