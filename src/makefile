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
JAR = Geoscape.jar
LIBPATH = lib/native
MAIN = core.Top
MANIFEST = manifest.mf
MKDIR = mkdir -p

LIBJARS = $(wildcard lib/*.jar)
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

# Build the application class files.
build: clean
	@$(MKDIR) $(BUILDPATH)

	@echo -n "Compiling Java source files..."
	@javac -d $(BUILDPATH) -s $(BUILDPATH) -h $(BUILDPATH) -classpath $(CLASSPATH) $(SOURCES)
	@echo "done."

# Build the application JAR file.
jar: build
	@echo -n "Generating \"$(JAR)\"..."

	@# Create a temporary JAR manifest file.
	@echo "Manifest-Version: 1.0" > $(MANIFEST)
	@echo "Class-Path: $(LIBJARS)" >> $(MANIFEST)
	@echo "Main-Class: $(MAIN)" >> $(MANIFEST)

	@# Run the Java archiver.
	@jar cfm $(JAR) $(MANIFEST) -C $(BUILDPATH) .
	@echo "done."

	@# Clean the temporary manifest.
	@$(DELETE) $(MANIFEST)

# Generate the documentation.
doc: FORCE
	@$(MKDIR) $(DOCPATH)

	@echo "Generating Javadoc documentation..."
	@javadoc -private -classpath $(CLASSPATH) -d doc $(SOURCES)
	@echo "The updated documentation is available in the $(DOCPATH)/ directory."

# Delete the build directory.
clean: FORCE
	@$(DELETE) $(BUILDPATH)

# Nonexistent file target.
FORCE: