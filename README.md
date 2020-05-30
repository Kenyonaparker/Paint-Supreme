# Microsoft Paint Reboot

My personal version of Microsoft Paint built from scratch using JavaFX programmng language. Custom tool bars that give you 
the ability to manipulate any picture object. Shapes can be added to the canvas in a range of colors all while the threading 
log keeping track of each action displayed on the canvas. 

## Installation

Use any IDE that will allow you to view JavaFX object oriented programming and will make it easy to import 
all extensions necessary for application to work properly. 

```package paintsupreme;

import java.io.File;
import java.io.IOException;
import java.util.Stack;
import java.util.logging.FileHandler;
import java.util.logging.Level;
```

## Technologies

netbeans, IntelliJ if you are coming from C language Eclipse
BlueJ is also a good IDE for JavaFx

## Launch
In order for the application to run properly you will need all three classes under one program: Bars.java, Polygons.java, Stages.java
PaintSupreme.java is the main page
```public class PaintSupreme {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Application.launch(Stages.class, args);
    }
    
    public class Bars {

    File selectedFile;
    FileChooser fileChooser;
    StackPane sp;
    
    public class Stages extends Application {

    File selectedFile;
    FileChooser fileChooser;
    StackPane sp;
    
    public class Polygons extends Stages {

    private Text actionStatus;
    private int polygonSides;
    private Polygon poly;
    ```
