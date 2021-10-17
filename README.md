# SENG201 21S1 Group 8 Project

The project's repository can be found on GitLab here: https://github.com/TheDeathlyCow/seng201-project/

Made by Michael Alpenfels and Jakib Isherwood.

# Running the Application

Simply double clicking the file `seng201-21s1-group-8.jar` should be enough to run the application. If that does not work, open a terminal or command prompt window in the same directory as the jar file and run the command `java -jar seng201-21s1-group-8.jar`. If you want to run the command line application, run the command `java -jar seng201-21s1-group-8.jar nogui` instead. 

# Build Instructions

This project was built using Maven. See below for build information in specific IDEs. **Ensure that when you import this project into an IDE like IntelliJ or Eclipse, that you import as a Maven project**.

**IMPORTANT NOTE**

The IntelliJ and Eclipse versions of this code are different. In order to build the code correctly, ensure that you are using the correct version of the code. Both will be included in the submission package. The IntelliJ version is our proper submission and the submitted JAR file is built from the IntelliJ version, but the Eclipse version should be identical to the IntelliJ version except for the additions needed to work in Eclipse. 

## IntelliJ

Build from code in `INTELLIJ-CODE-island-trader.zip`, or from the code [on the master branch on GitLab](https://eng-git.canterbury.ac.nz/mwa172/seng201-21s1-group-8/-/tree/master).

First, build the project by executing the **Build** command either by selecting **Build | Build Project** from the top bar or by pressing `ctrl+f9` (on Windows). 

After compiling the project, create an artifact by opening the project strucutre menu in **File | Project Structure** (or press `ctrl+alt+shift+s`), then select **Artifacts** on the left panel. Click the `+` and hover **JAR**, and select `From modules with dependencies...`. Select the class `Main` in the package `nz.ac.gitlab.mwa172.seng201.group8`. Make sure that this artifact has all of the required dependencies, and create and apply the artifact. Once the artifact is created, it can be built into a jar by selecting **Build | Build Artifacts...** from the main menu, then hover over the `.jar` you want to build and select the `Build` option. The output JAR should be located in `out/artifacts`. 

The above instructions are based on the article from JetBrains, and they are probably the best reference for build instructions: https://www.jetbrains.com/help/idea/compiling-applications.html#package_into_jar

## Eclipse

Build from code in `ECLIPSE-CODE-island-trader.zip`, or from the code [on the eclipse branch on GitLab](https://eng-git.canterbury.ac.nz/mwa172/seng201-21s1-group-8/-/tree/eclipse).

To export the project from Eclipse, first ensure that you are actually using the eclipse version of the project. There are some differences between the Eclipse and IntelliJ versions of the code that may cause the output jar to not work. Specifically, for some reason the Eclipse version just will not work unless the `resources` folder is moved directly into the `bin`. I have no idea why this is the case. Also, the code from the IntelliJ GUI designer must be generated into the .jar files directly in the Eclipse version, and this also requires an additional dependency (see below for more details). 

Building from Eclipse is pretty straightforward. Select `File | Export...`, select `Java / Runnable JAR File`, pick a location to export to, and ensure that library handling is set to "Extract required libraries into generated JAR", and click Finish. The output JAR should appear in the output folder you selected.

# Dependencies

This application has the following dependencies: 

* JUnit 4 and 5. 
* Gson 2.8.5: https://mvnrepository.com/artifact/com.google.code.gson/gson/2.8.5
* FlatLaf 1.1.2: https://search.maven.org/artifact/com.formdev/flatlaf/1.2/jar
* FlatLaf IntelliJ themes 1.1.2: https://search.maven.org/artifact/com.formdev/flatlaf-intellij-themes/1.2/jar 

The maven dependencies are as follows (from `pom.xml`):

```xml
<dependencies>
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.12</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>RELEASE</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>com.google.code.gson</groupId>
        <artifactId>gson</artifactId>
        <version>2.8.5</version>
    </dependency>
    <dependency>
        <groupId>com.formdev</groupId>
        <artifactId>flatlaf</artifactId>
        <version>1.1.2</version>
    </dependency>
    <dependency>
        <groupId>com.formdev</groupId>
        <artifactId>flatlaf-intellij-themes</artifactId>
        <version>1.1.2</version>
    </dependency>
</dependencies>
```

## Eclipse Dependencies

In order to properly generate the GUI code for Eclipse, an additional dependency is needed. 

The maven dependency is as follows (from `pom.xml`):

```xml
<dependency>
    <groupId>com.intellij</groupId>
    <artifactId>forms_rt</artifactId>
    <version>7.0.3</version>
</dependency>
```

The following plugin is also required (included in `pom.xml`):

```xml
<build>
    <pluginManagement>
        <plugins>
            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>ideauidesigner-maven-plugin</artifactId>
                <version>1.0-beta-1</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>javac2</goal>
                        </goals>
                    </execution>
                </executions>

                <configuration>
                    <fork>true</fork>
                    <debug>true</debug>
                    <failOnError>true</failOnError>
                </configuration>
            </plugin>
        </plugins>
    </pluginManagement>
</build>
```
