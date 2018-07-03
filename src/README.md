# Overview
There are several ways to run Geoscape.  Note that all of the methods listed below assume that the entire **src/** Geoscape directory is downloaded locally.
1. Run `java -jar Geoscape.jar` in CMD or your favourite terminal.
    * This is the recommended way to launch Geoscape.
1. Double-click on **Geoscape.jar** from a file explorer GUI.
    * Unfortunately, no `Info`, `Error`, etc. logs will be displayed using this approach.
1. Run `make run`.
    * This will compile Geoscape and run the application from the generated `.class` files.
    * See the **Compilation** section below for more information about compiling Geoscape.

## Controls
The following controls are printed to `stdout` when Geoscape is launched from the command line:

| **Primary Key**      | **Secondary Key**     | **Description**  |
| :---:                | :---:                 | :---             |
| <kbd>Esc</kbd>       | <kbd>P</kbd>          | Pause            |
| <kbd>W</kdb>         | <kbd>&#8593;</kbd>    | Move forward     |
| <kbd>S</kdb>         | <kbd>&#8595;</kbd>    | Move backward    |
| <kbd>A</kdb>         | <kbd>&#8592;</kbd>    | Strafe left      |
| <kbd>D</kdb>         | <kbd>&#8594;</kbd>    | Strafe right     |
| <kbd>Space</kdb>     |                       | Ascend           |
| Left <kbd>Ctrl</kdb> | Right <kbd>Ctrl</kbd> | Descend          |
| <kbd>+</kdb>         |                       | Speed up         |
| <kbd>-</kdb>         |                       | Slow down        |
| Mouse                |                       | Look around      |
| <kbd>V</kdb>         |                       | Toggle Vsync     |

## Compilation
Compilation is currently supported for Windows and Linux distributions.  However, there are a few caveats:
* This project only compiles with **Java 1.8** or higher.  You can check your Java version by runnning:
  * `java -version`
  * `javac -version`
* Windows users must have Cygwin installed and added to their path.

### Makefile Targets
Here is a list of supported **makefile** targets:
1. `make clean`: Cleans the build directory.
1. `make build`: Compiles the Java source files.
1. `make run`:   Runs the application.
1. `make jar`:   Creates an executable JAR file.
1. `make doc`:   Generates the Javadoc documentation.
