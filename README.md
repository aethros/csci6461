# csci6461
csci6461 java cisc cpu vm

## Getting Started:
To build:

 - This program uses maven as the build system. Please ensure java JDK and maven are installed and working properly. Then run `mvn -f architecture/pom.xml clean compile package` from the root directory of this repo.


To run:

 - Execute the following command from the root directory of this repo. You can then attach to the application over port `5050` to begin debugging. Omit the `-agentlib:...` flag if you do not wish to debug, and simply wish to run the application.

```shell
java \
    -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=localhost:5050 \
    -XX:+ShowCodeDetailsInExceptionMessages \
    --module-path \
        $PWD/architecture/target/classes:$M2_REPO/org/openjfx/javafx-base/13/javafx-base-13-linux.jar:$M2_REPO/org/openjfx/javafx-fxml/13/javafx-fxml-13-linux.jar:$M2_REPO/org/openjfx/javafx-controls/13/javafx-controls-13-linux.jar:$M2_REPO/org/openjfx/javafx-graphics/13/javafx-graphics-13-linux.jar:$M2_REPO/org/projectlombok/lombok/1.18.30/lombok-1.18.30.jar \
    --add-modules \
        edu.gwu.seas.csci.architecture6461 \
    -classpath \
        $M2_REPO/org/openjfx/javafx-base/13/javafx-base-13.jar:$M2_REPO/org/openjfx/javafx-fxml/13/javafx-fxml-13.jar:$M2_REPO/org/openjfx/javafx-controls/13/javafx-controls-13.jar:$M2_REPO/org/openjfx/javafx-graphics/13/javafx-graphics-13.jar \
    --module \
        edu.gwu.seas.csci.architecture6461/edu.gwu.seas.csci.architecture6461.App \
    -jar \
        architecture/target/architecture-1.0.0.jar;
```

## Program Details
This is a simple CPU simulator set up with a Model-View-Controller pattern. Each View consumes a model, and transforms it from the controller.

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

State management is handled by a Manager class. The manager is a singleton (only one ever exists) which manages all state throughout the application. This includes behavior such as starrting, stopping, and resetting the application.

Ex.:
```
App
 `-- Program Manager
```