# Windows Instructions

## Prerequisites:

 - For those attempting to run this application on a windows machine, please use the following instructions:
    - Please ensure JDK 11 or greater is installed and on the path.
    - Please ensure maven is installed and on the path.
    - Please set the enviornment variable `$M2_REPO` to the location of your maven repository. (Typically this is located at `$HOME\.m2\repository\`.)

## To Build:

 - You may use the `run.ps1` script from the console, which will build and run the application.
 - The same build command listed in the readme applies for windows, given the above prerequisites. Run `mvn -f architecture\\pom.xml clean compile package` from the root directory of this repo.

## To Run:

 - Execute the following command from the root directory of this repository. This links all third-party JARfiles downloaded during build for the runtime to work.
 - You can then attach to the application over port `5050` to begin debugging. Omit the `-agentlib:...` flag if you do not wish to debug, and simply wish to run the application.
 - *Note:* Some windows terminals do not allow multi-line commands, and so in that instance, please replace ` \\n ` with ` ` when you paste in the command.

```powershell
java \
    -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=localhost:5050 \
    --module-path \
        "$PWD\\architecture\\target\\classes;$M2_REPO\\org\\openjfx\\javafx-controls\\17.0.10\\javafx-controls-17.0.10-win.jar;$M2_REPO\\org\\openjfx\\javafx-graphics\\17.0.10\\javafx-graphics-17.0.10-win.jar;$M2_REPO\\org\\openjfx\\javafx-base\\17.0.10\\javafx-base-17.0.10-win.jar;$M2_REPO\\org\\openjfx\\javafx-fxml\\17.0.10\\javafx-fxml-17.0.10-win.jar;$M2_REPO\\org\\projectlombok\\lombok\\1.18.30\\lombok-1.18.30.jar" \
    --add-modules \
        edu.gwu.seas.csci.architecture6461 \
    -classpath \
        "$M2_REPO\\org\\openjfx\\javafx-controls\\17.0.10\\javafx-controls-17.0.10.jar;$M2_REPO\\org\\openjfx\\javafx-graphics\\17.0.10\\javafx-graphics-17.0.10.jar;$M2_REPO\\org\\openjfx\\javafx-base\\17.0.10\\javafx-base-17.0.10.jar;$M2_REPO\\org\\openjfx\\javafx-fxml\\17.0.10\\javafx-fxml-17.0.10.jar" \
    --module \
        'edu.gwu.seas.csci.architecture6461/edu.gwu.seas.csci.architecture6461.App' \
    -jar \
        "architecture\\target\\architecture-1.0.0.jar";
```