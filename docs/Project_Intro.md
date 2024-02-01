# Program Overview
## Git
 - Git is a version control system (VCS) which allows people to work on a project at the same time
 - Users can checkout "branches", or working copies of their source code
 - When they finish their piece of work, they can "merge" their branch into the main working copy
## Java
 - Java is an object-oriented programming language, similar to c++ or python.
 - Java runs in a runtime called the Java Virtual Machine (JVM), which is called with the command `java` from the terminal
## VSCode
 - VSCode is a text editor which you can add extensions onto, allowing you to understand your code
 - VSCode has extensions for java, maven, XML, and other tools
 - Other text editors or IDEs can also be used to develop java applications
## Source Code
 - The source code is a JavaFX application. This allows you to design your application views visually with tools which output XML code
 - Each window houses a view, which is backed by a controller (logic), and a model (data) -- This pattern is called MVC
## Application structure
 - This application has a main window, which has a front panel
 - Each front panel has a series of registers, which represent working memory in a CPU
 - Each register has a label, a series of lights indicating the state of the register, and a load button to update the state of the register.
 - The application state (overall system state and logic) is handled by a "Manager" class, which is a singleton
 - Only one instance of a singleton can be created

# Desired learning objectives
## Learn how Git works
 - Clone a repository
 - Checkout code
 - Create a branch
## Learn how GitHub can manage a project
 - Create a pull request
 - Watch pipelines run verify code works
 - Comment on pull requests
 - Approve & Merge code
## Learn basic Java
 - Setup an IDE
    - Extensions I use with VSCode:
    ```text
        GitHub.copilot
        GitHub.copilot-chat
        github.vscode-github-actions
        redhat.java
        redhat.vscode-xml
        SonarSource.sonarlint-vscode
        VisualStudioExptTeam.intellicode-api-usage-examples
        VisualStudioExptTeam.vscodeintellicode
        vscjava.vscode-java-debug
        vscjava.vscode-java-dependency
        vscjava.vscode-java-pack
        vscjava.vscode-java-test
        vscjava.vscode-maven
    ```
 - Build an application in `mvn`
 - Run an application with `java`
## Setup application debugger
 - Attach a debugger and step through code
 - Set break points
 - Inspect variables at runtime
## Learn JavaFX and MVC pattern
 - Understand FXML / Scene Builder
 - Understand the role of View
 - Understand the role of Models
 - Understand the role of a Controller
## Approve pull request
 - Approve pull request when you feel like you know enough to start working on project
