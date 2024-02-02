# csci6461
csci6461 java cisc cpu vm

## Getting Started:
To build:

 - This program uses maven as the build system. Please ensure java JDK and maven are installed and working properly. Then run `mvn -f architecture/pom.xml clean compile package` from the root directory of this repo.


To run:

 - Execute the following command from the root directory of this repo. You can then attach to the application over port `5050` to begin debugging. Omit the `-agentlib:...` flag if you do not wish to debug, and simply wish to run the application. Ensure you have defined `$M2_REPO` for your enviornment.

```shell
java \
    -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=localhost:5050 \
    -XX:+ShowCodeDetailsInExceptionMessages \
    --module-path \
        $PWD/architecture/target/classes:$M2_REPO/org/openjfx/javafx-base/17.0.10/javafx-base-17.0.10-linux.jar:$M2_REPO/org/openjfx/javafx-fxml/17.0.10/javafx-fxml-17.0.10-linux.jar:$M2_REPO/org/openjfx/javafx-controls/17.0.10/javafx-controls-17.0.10-linux.jar:$M2_REPO/org/openjfx/javafx-graphics/17.0.10/javafx-graphics-17.0.10-linux.jar:$M2_REPO/org/projectlombok/lombok/1.18.30/lombok-1.18.30.jar \
    --add-modules \
        edu.gwu.seas.csci.architecture6461 \
    -classpath \
        $M2_REPO/org/openjfx/javafx-base/17.0.10/javafx-base-17.0.10.jar:$M2_REPO/org/openjfx/javafx-fxml/17.0.10/javafx-fxml-17.0.10.jar:$M2_REPO/org/openjfx/javafx-controls/17.0.10/javafx-controls-17.0.10.jar:$M2_REPO/org/openjfx/javafx-graphics/17.0.10/javafx-graphics-17.0.10.jar \
    --module \
        edu.gwu.seas.csci.architecture6461/edu.gwu.seas.csci.architecture6461.App \
    -jar \
        architecture/target/architecture-1.0.0.jar;
```

CLI Arguments Explanation:

```shell
java                # Command to start JDK Runtime Environment
    -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=localhost:5050
                    # Java Debug Wire Protocol option, with Debug Transport type `socket`. Address set to local port.
    -XX:+ShowCodeDetailsInExceptionMessages
                    # Set exception messages to contain snippets of the code where the Exception was raised.
    --module-path   # A `:` separated list of directories, each directory is a directory of modules.
    --add-modules   # Root modules to resolve in addition to the initial module. <module name> can also be ALL-DEFAULT, ALL-SYSTEM, ALL-MODULE-PATH.
    -classpath      # A `:` separated list of directories, JAR archives, and ZIP archives to search for class files.
    --module        # <module>[/<mainclass>] [args...] (to execute the main class in a module)
    -jar            # <jarfile> [args...] (to execute a jar file)
```

## Program Details
The following information is a brief overview of the application. For more detailed information, see the `./docs/` directory.

This is a simple CPU simulator set up with a Model-View-Controller pattern. Each View displays a data model, and transforms it via the controller.

Ex.:
```
App
 |-- CPU View + .fxml
 |   |-- CPU Controller
 |   `-- CPU Model
 `-- Register View + .fxml
     |-- Register Controller
     `-- Register Model
```

State management is handled by Manager classes. The session manager is a singleton (only one ever exists) which manages all state throughout the application. This includes behavior such as starrting, stopping, and resetting application state. There are other managers which handle state for specific functions or behaviors. For example, the control unit manages machine instructions, machine memory, and other functions. The assembler manages the process of converting assembly instructions to machine code.

Ex.:
```
App
 |-- Session Manager
 |-- Control Unit
 `-- Assembler
```