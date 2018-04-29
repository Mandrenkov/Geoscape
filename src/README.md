## Overview
Compilation is currently supported for Windows and Linux distributions.  However, there are a few caveats:
* This project only compiles with **Java 1.8** or higher.  You can check your Java version by runnning:
  * `java -version`
  * `javac -version`
* Windows users must have Cygwin installed and added to their path.

## Make Targets
1. `make clean`: Cleans the build directory.
2. `make build`: Compiles the Java source files.
3. `make run`:   Runs the application.
