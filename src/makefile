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
LIBPATH = lib/native
MAIN = core.Top
MKDIR = mkdir -p
RM = rm -rf

# Lists of Java source and target files.
SOURCES = $(wildcard */*.java)
CLASSES = $(patsubst %.java, $(BUILDPATH)/%.class, $(SOURCES))

# List of Java source files destined for compilation.
TARGETS =

# Variables whose values depend on the current platform.
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
build: $(CLASSES)
	@$(MKDIR) $(BUILDPATH)

	@if [ -z "$(TARGETS)" ]; then \
		echo "Skipping compilation: all targets are up-to-date." ; \
	else \
		echo "Compiling:$(TARGETS)." ; \
		javac -d $(BUILDPATH) -s $(BUILDPATH) -h $(BUILDPATH) -classpath $(CLASSPATH) $(TARGETS) ; \
	fi

# Add the Java source file dependency to the compilation list.
$(BUILDPATH)/%.class: %.java
	$(eval TARGETS += $<)

# Delete the build directory.
clean:
	@$(RM) $(BUILDPATH)