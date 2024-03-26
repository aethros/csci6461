#!/usr/bin/env bash
M2_REPO="$HOME/.m2/repository";
MODPATH_DARWIN="$PWD/architecture/target/classes:$M2_REPO/org/openjfx/javafx-base/17.0.10/javafx-base-17.0.10-mac.jar:$M2_REPO/org/openjfx/javafx-fxml/17.0.10/javafx-fxml-17.0.10-mac.jar:$M2_REPO/org/openjfx/javafx-controls/17.0.10/javafx-controls-17.0.10-mac.jar:$M2_REPO/org/openjfx/javafx-graphics/17.0.10/javafx-graphics-17.0.10-mac.jar:$M2_REPO/org/projectlombok/lombok/1.18.30/lombok-1.18.30.jar";
MODPATH="$PWD/architecture/target/classes:$M2_REPO/org/openjfx/javafx-base/17.0.10/javafx-base-17.0.10-linux.jar:$M2_REPO/org/openjfx/javafx-fxml/17.0.10/javafx-fxml-17.0.10-linux.jar:$M2_REPO/org/openjfx/javafx-controls/17.0.10/javafx-controls-17.0.10-linux.jar:$M2_REPO/org/openjfx/javafx-graphics/17.0.10/javafx-graphics-17.0.10-linux.jar:$M2_REPO/org/projectlombok/lombok/1.18.30/lombok-1.18.30.jar";
CLASSPATH="$M2_REPO/org/openjfx/javafx-base/17.0.10/javafx-base-17.0.10.jar:$M2_REPO/org/openjfx/javafx-fxml/17.0.10/javafx-fxml-17.0.10.jar:$M2_REPO/org/openjfx/javafx-controls/17.0.10/javafx-controls-17.0.10.jar:$M2_REPO/org/openjfx/javafx-graphics/17.0.10/javafx-graphics-17.0.10.jar";
JARFILE="architecture/target/architecture-1.0.0.jar";

if ! test -f $JARFILE; then
    mvn -f architecture/pom.xml clean compile package
fi

if [[ $(uname) == "Darwin" ]]; then
    java --module-path $MODPATH_DARWIN --add-modules edu.gwu.seas.csci.architecture6461 -classpath $CLASSPATH --module edu.gwu.seas.csci.architecture6461/edu.gwu.seas.csci.architecture6461.MainClass -jar $JARFILE;
else
    java --module-path $MODPATH --add-modules edu.gwu.seas.csci.architecture6461 -classpath $CLASSPATH --module edu.gwu.seas.csci.architecture6461/edu.gwu.seas.csci.architecture6461.MainClass -jar $JARFILE;
fi
