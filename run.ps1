$M2_REPO = "$HOME\.m2\repository"
$MODPATH = "$PWD\architecture\target\classes;$M2_REPO\org\openjfx\javafx-controls\17.0.10\javafx-controls-17.0.10-win.jar;$M2_REPO\org\openjfx\javafx-graphics\17.0.10\javafx-graphics-17.0.10-win.jar;$M2_REPO\org\openjfx\javafx-base\17.0.10\javafx-base-17.0.10-win.jar;$M2_REPO\org\openjfx\javafx-fxml\17.0.10\javafx-fxml-17.0.10-win.jar;$M2_REPO\org\projectlombok\lombok\1.18.30\lombok-1.18.30.jar"
$CLASSPATH = "$M2_REPO\org\openjfx\javafx-controls\17.0.10\javafx-controls-17.0.10.jar;$M2_REPO\org\openjfx\javafx-graphics\17.0.10\javafx-graphics-17.0.10.jar;$M2_REPO\org\openjfx\javafx-base\17.0.10\javafx-base-17.0.10.jar;$M2_REPO\org\openjfx\javafx-fxml\17.0.10\javafx-fxml-17.0.10.jar"
$JARFILE = ".\architecture\target\architecture-1.0.0.jar"

if (!$(Test-Path $JARFILE)) {
  mvn -f "architecture\pom.xml" clean compile package
}

java --module-path $MODPATH --add-modules edu.gwu.seas.csci.architecture6461 -classpath $CLASSPATH --module 'edu.gwu.seas.csci.architecture6461/edu.gwu.seas.csci.architecture6461.MainClass' -jar $JARFILE;