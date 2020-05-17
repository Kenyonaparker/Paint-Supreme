/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paintsupreme;

import java.io.File;
import java.io.IOException;
import java.util.Stack;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 *
 * @author Kenyona
 */
public class Bars {

    File selectedFile;
    FileChooser fileChooser;
    StackPane sp;
    ToolBar toolBar;
    ToggleButton linebtn;
    ToggleButton drawbtn;
    ToggleButton eraserbtn;
    ToggleButton rectbtn;
    ToggleButton circlebtn;
    ToggleButton ellipsebtn;
    ToggleButton polygon;
    ToggleButton square;
    ToggleButton textbtn;
    ToggleButton zoomin;
    ToggleButton zoomout;
    ToggleButton dropper;
    ToggleButton selected;
    ToggleButton moved;
    ToggleButton paste;
    Button undo;
    Button redo;
    Text TOOL;
    Text ToolLabel;
    TextArea text;
    Ellipse ellipse;
    Ellipse elps;
    Circle circ;
    Line line;
    Slider wid;
    Thread t;

    final static Logger logger = Logger.getLogger(Bars.class.getName());
    private Text actionStatus;
    private int polygonSides;
    private Polygon poly;
    private Rectangle rect;
    private Rectangle Rect;
    private Rectangle selectRect;
    private ImageView cropImage;
    private final int POLY_WINDOW_WIDTH = 450;
    private final int POLY_WINDOW_HEIGHT = 150;
    private final int NEW_WINDOW_WIDTH = 400;
    private final int NEW_WINDOW_HEIGHTH = 150;
    private final double NOTES_WINDOW_WIDTH = 750;
    private final double NOTES_WINDOW_HEIGHT = 750;
    private final double TOOLS_WINDOW_WIDTH = 750;
    private final double TOOLS_WINDOW_HEIGHT = 550;
    private Stack<Image> actionsU = new Stack();
    private Stack<Image> actionsR = new Stack();
    private double startX;
    private double startY;
    private double mouseClickX;
    private double mouseClickY;
    private double xClick;
    private double yClick;
    private Pane p;
    private GraphicsContext gc;
    private ColorPicker colorPicker;
    ColorPicker cpLine = new ColorPicker(Color.BLACK);
    ColorPicker cpFill = new ColorPicker(Color.TRANSPARENT);
    Stack<Shape> undoHistory = new Stack();
    Stack<Shape> redoHistory = new Stack();

    
    public void pushToHistory(Shape o){
            undoHistory.push(o);
        }
    public ToolBar CreateToolBar(Scene scene, Stage Primarystage, GraphicsContext gc) {
        this.gc = gc;
        ToolBar createtoolBar = new ToolBar();

        FileHandler handler;
        try {
            handler = new FileHandler("default.log", true);
            logger.addHandler(handler);
        } catch (IOException | SecurityException ex) {
            Logger.getLogger(Bars.class.getName()).log(Level.SEVERE, null, ex);
        }
        //ToggleButton is better for shapes because you can go back and forth easier.
        linebtn = new ToggleButton("Line");
        drawbtn = new ToggleButton("Draw");
        eraserbtn = new ToggleButton("Eraser");
        rectbtn = new ToggleButton("Rectangle");
        circlebtn = new ToggleButton("Circle");
        ellipsebtn = new ToggleButton("Ellipse");
        polygon = new ToggleButton("Polygon");
        square = new ToggleButton("Square");
        
        
        //The slider controls the width of my lines
        wid = new Slider();
        wid.setMin(1);
        wid.setMax(40);
        wid.setValue(20);
        //This is the color picker grid on the first toolbar        
        colorPicker = new ColorPicker();
        colorPicker.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                Color c = colorPicker.getValue();
                System.out.println("New Color's RGB=" + c.getRed() + " " + c.getGreen() + " " + c.getBlue());
            }
        });
        //This is where my shape coding begins
        line = new Line();
        rect = new Rectangle();
        circ = new Circle();
        elps = new Ellipse();
    
        
        createtoolBar.getItems().addAll(wid, colorPicker, linebtn, drawbtn, eraserbtn, rectbtn, circlebtn, ellipsebtn, polygon, square);

        linebtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ToolLabel.setText("linebtn");
                t = new Thread(new ToolThread());
                t.start();
                setUnselected();
                linebtn.setSelected(true);
            }
        });
        drawbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ToolLabel.setText("drawbtn");
                t = new Thread(new ToolThread());
                t.start();
                setUnselected();
                drawbtn.setSelected(true);
            }
        });
        eraserbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ToolLabel.setText("eraserbtn");
                t = new Thread(new ToolThread());
                t.start();
                setUnselected();
                eraserbtn.setSelected(true);
            }
        });
        rectbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ToolLabel.setText("rectbtn");
                t = new Thread(new ToolThread());
                t.start();
                setUnselected();
                rectbtn.setSelected(true);
            }
        });
        circlebtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ToolLabel.setText("circlebtn");
                t = new Thread(new ToolThread());
                t.start();
                setUnselected();
                circlebtn.setSelected(true);
            }
        });
        ellipsebtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ToolLabel.setText("ellipsebtn");
                t = new Thread(new ToolThread());
                t.start();
                setUnselected();
                ellipsebtn.setSelected(true);
            }
        });
        square.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ToolLabel.setText("square");
                t = new Thread(new ToolThread());
                t.start();
                setUnselected();
                square.setSelected(true);
            }
        });
        polygon.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ToolLabel.setText("polygon");
                t = new Thread(new ToolThread());
                t.start();
                setUnselected();
                polygon.setSelected(true);

            }
        });
        return createtoolBar;
    }

    /* @param scene
        * @param Primarystage
        * @return 
     */
    public ToolBar SecondToolBar(Scene scene, Stage Primarystage, Canvas canvas) {
        ToolBar secondtoolBar = new ToolBar();

        textbtn = new ToggleButton("Textbtn");
        zoomin = new ToggleButton("Zoom In");
        zoomout = new ToggleButton("Zoom Out");
        dropper = new ToggleButton("Dropper");
        selected = new ToggleButton("Selected");
        moved = new ToggleButton("Move");
        paste = new ToggleButton("Paste");
        undo = new Button("Undo");
        redo = new Button("Redo");
        //This is my variable necessary for my text button
        text = new TextArea();
        text.setPrefRowCount(1);
        TOOL = new Text("TOOL : ");
        ToolLabel = new Text("None");

        secondtoolBar.getItems().addAll(textbtn, zoomin, zoomout, dropper, selected, moved, paste, undo,
                redo, TOOL, ToolLabel, text);

        //\\\All of these action events are to make sure the it will release the isSelected///\\\\\
        //\\\for each button when the next button is selected.////\\\\
        textbtn.setOnAction((ActionEvent event) -> {
            ToolLabel.setText("textbtn");
            t = new Thread(new ToolThread());
            t.start();
            setUnselected();
            textbtn.setSelected(true);
        });
        dropper.setOnAction((ActionEvent event) -> {
            ToolLabel.setText("dropper");
            t = new Thread(new ToolThread());
            t.start();
            setUnselected();
            dropper.setSelected(true);
        });
        moved.setOnAction((ActionEvent event) -> {
            ToolLabel.setText("moved");
            t = new Thread(new ToolThread());
            t.start();
            setUnselected();
            moved.setSelected(true);
        });
        paste.setOnAction((ActionEvent event) -> {
            ToolLabel.setText("paste");
            t = new Thread(new ToolThread());
            t.start();
            setUnselected();
            paste.setSelected(true);
        });
        selected.setOnAction((ActionEvent event) -> {
            ToolLabel.setText("textbtn");
            t = new Thread(new ToolThread());
            t.start();
            setUnselected();
            selected.setSelected(true);
        });
        undo.setOnAction(e->{
            if(!undoHistory.empty()){
                gc.clearRect(0, 0, 1080, 790);
                Shape removedShape = undoHistory.lastElement();
                if(removedShape.getClass() == Line.class) {
                    Line tempLine = (Line) removedShape;
                    tempLine.setFill(gc.getFill());
                    tempLine.setStroke(gc.getStroke());
                    tempLine.setStrokeWidth(gc.getLineWidth());
                    redoHistory.push(new Line(tempLine.getStartX(), tempLine.getStartY(), tempLine.getEndX(), tempLine.getEndY()));
                    
                }
                else if(removedShape.getClass() == Rectangle.class) {
                    Rectangle tempRect = (Rectangle) removedShape;
                    tempRect.setFill(gc.getFill());
                    tempRect.setStroke(gc.getStroke());
                    tempRect.setStrokeWidth(gc.getLineWidth());
                    redoHistory.push(new Rectangle(tempRect.getX(), tempRect.getY(), tempRect.getWidth(), tempRect.getHeight()));
                }
                else if(removedShape.getClass() == Circle.class) {
                    Circle tempCirc = (Circle) removedShape;
                    tempCirc.setStrokeWidth(gc.getLineWidth());
                    tempCirc.setFill(gc.getFill());
                    tempCirc.setStroke(gc.getStroke());
                    redoHistory.push(new Circle(tempCirc.getCenterX(), tempCirc.getCenterY(), tempCirc.getRadius()));
                }
                else if(removedShape.getClass() == Ellipse.class) {
                    Ellipse tempElps = (Ellipse) removedShape;
                    tempElps.setFill(gc.getFill());
                    tempElps.setStroke(gc.getStroke());
                    tempElps.setStrokeWidth(gc.getLineWidth());
                    redoHistory.push(new Ellipse(tempElps.getCenterX(), tempElps.getCenterY(), tempElps.getRadiusX(), tempElps.getRadiusY()));
                }
                Shape lastRedo = redoHistory.lastElement();
                lastRedo.setFill(removedShape.getFill());
                lastRedo.setStroke(removedShape.getStroke());
                lastRedo.setStrokeWidth(removedShape.getStrokeWidth());
                undoHistory.pop();
                
                for(int i=0; i < undoHistory.size(); i++) {
                    Shape shape = undoHistory.elementAt(i);
                    if(shape.getClass() == Line.class) {
                        Line temp = (Line) shape;
                        gc.setLineWidth(temp.getStrokeWidth());
                        gc.setStroke(temp.getStroke());
                        gc.setFill(temp.getFill());
                        gc.strokeLine(temp.getStartX(), temp.getStartY(), temp.getEndX(), temp.getEndY());
                    }
                    else if(shape.getClass() == Rectangle.class) {
                        Rectangle temp = (Rectangle) shape;
                        gc.setLineWidth(temp.getStrokeWidth());
                        gc.setStroke(temp.getStroke());
                        gc.setFill(temp.getFill());
                        gc.fillRect(temp.getX(), temp.getY(), temp.getWidth(), temp.getHeight());
                        gc.strokeRect(temp.getX(), temp.getY(), temp.getWidth(), temp.getHeight());
                    }
                    else if(shape.getClass() == Circle.class) {
                        Circle temp = (Circle) shape;
                        gc.setLineWidth(temp.getStrokeWidth());
                        gc.setStroke(temp.getStroke());
                        gc.setFill(temp.getFill());
                        gc.fillOval(temp.getCenterX(), temp.getCenterY(), temp.getRadius(), temp.getRadius());
                        gc.strokeOval(temp.getCenterX(), temp.getCenterY(), temp.getRadius(), temp.getRadius());
                    }
                    else if(shape.getClass() == Ellipse.class) {
                        Ellipse temp = (Ellipse) shape;
                        gc.setLineWidth(temp.getStrokeWidth());
                        gc.setStroke(temp.getStroke());
                        gc.setFill(temp.getFill());
                        gc.fillOval(temp.getCenterX(), temp.getCenterY(), temp.getRadiusX(), temp.getRadiusY());
                        gc.strokeOval(temp.getCenterX(), temp.getCenterY(), temp.getRadiusX(), temp.getRadiusY());
                    }
                }
            } else {
                System.out.println("there is no action to undo");
            }
                
            ToolLabel.setText("undo");
            t = new Thread(new ToolThread());
            t.start();
            setUnselected();
            selected.setSelected(true);
        });
         redo.setOnAction(e->{
            if(!redoHistory.empty()) {
                Shape shape = redoHistory.lastElement();
                gc.setLineWidth(shape.getStrokeWidth());
                gc.setStroke(shape.getStroke());
                gc.setFill(shape.getFill());
                    
                redoHistory.pop();
                if(shape.getClass() == Line.class) {
                    Line tempLine = (Line) shape;
                    gc.strokeLine(tempLine.getStartX(), tempLine.getStartY(), tempLine.getEndX(), tempLine.getEndY());
                    undoHistory.push(new Line(tempLine.getStartX(), tempLine.getStartY(), tempLine.getEndX(), tempLine.getEndY()));
                }
                else if(shape.getClass() == Rectangle.class) {
                    Rectangle tempRect = (Rectangle) shape;
                    gc.fillRect(tempRect.getX(), tempRect.getY(), tempRect.getWidth(), tempRect.getHeight());
                    gc.strokeRect(tempRect.getX(), tempRect.getY(), tempRect.getWidth(), tempRect.getHeight());
                    
                    undoHistory.push(new Rectangle(tempRect.getX(), tempRect.getY(), tempRect.getWidth(), tempRect.getHeight()));
                }
                else if(shape.getClass() == Circle.class) {
                    Circle tempCirc = (Circle) shape;
                    gc.fillOval(tempCirc.getCenterX(), tempCirc.getCenterY(), tempCirc.getRadius(), tempCirc.getRadius());
                    gc.strokeOval(tempCirc.getCenterX(), tempCirc.getCenterY(), tempCirc.getRadius(), tempCirc.getRadius());
                    
                    undoHistory.push(new Circle(tempCirc.getCenterX(), tempCirc.getCenterY(), tempCirc.getRadius()));
                }
                else if(shape.getClass() == Ellipse.class) {
                    Ellipse tempElps = (Ellipse) shape;
                    gc.fillOval(tempElps.getCenterX(), tempElps.getCenterY(), tempElps.getRadiusX(), tempElps.getRadiusY());
                    gc.strokeOval(tempElps.getCenterX(), tempElps.getCenterY(), tempElps.getRadiusX(), tempElps.getRadiusY());
                    
                    undoHistory.push(new Ellipse(tempElps.getCenterX(), tempElps.getCenterY(), tempElps.getRadiusX(), tempElps.getRadiusY()));
                }
                Shape lastUndo = undoHistory.lastElement();
                lastUndo.setFill(gc.getFill());
                lastUndo.setStroke(gc.getStroke());
                lastUndo.setStrokeWidth(gc.getLineWidth());
            } else {
                System.out.println("there is no action to redo");
            }
            
            ToolLabel.setText("redo");
            t = new Thread(new ToolThread());
            t.start();
            setUnselected();
            selected.setSelected(true);
        });
        zoomin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                canvas.resize(canvas.getWidth() * .9, canvas.getHeight() * .9);
                Primarystage.setWidth(Primarystage.getWidth() * .9);
                Primarystage.setHeight(Primarystage.getHeight() * .9);
            }
        });
        zoomout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                canvas.resize(canvas.getWidth() * 1.1, canvas.getHeight() * 1.1);
                Primarystage.setWidth(Primarystage.getWidth() * 1.1);
                Primarystage.setHeight(Primarystage.getHeight() * 1.1);
            }
        });
        return secondtoolBar;
    }

    public void setUnselected() {
        linebtn.setSelected(false);
        drawbtn.setSelected(false);
        eraserbtn.setSelected(false);
        rectbtn.setSelected(false);
        circlebtn.setSelected(false);
        polygon.setSelected(false);
        square.setSelected(false);
        ellipsebtn.setSelected(false);
        textbtn.setSelected(false);
        dropper.setSelected(false);
        moved.setSelected(false);
        selected.setSelected(false);
        paste.setSelected(false);
    }

    /**
     * This method creates menuBar and has a junit test that passes
     *
     * @param scene
     * @param Primarystage
     * @param sp
     * @param iv
     * @param canvas
     * @param gc
     * @return
     */
    public MenuBar menuBar(Scene scene, Stage Primarystage, StackPane sp, ImageView iv, Canvas canvas, GraphicsContext gc) {
        this.sp = sp;
        MenuBar menuBar = new MenuBar();
        //This code is to add file to the menu bar.
        Menu menuFile = new Menu("File");
        Menu menuEdit = new Menu("Edit");
        Menu menuHelp = new Menu("Help");
        MenuItem openButton = new MenuItem("Open");
        MenuItem closeButton = new MenuItem("Close");
        MenuItem saveButton = new MenuItem("Save");
        MenuItem saveasButton = new MenuItem("Save As");
        MenuItem helpButton = new MenuItem("About Paint");
        MenuItem release = new MenuItem("Release Notes");
        MenuItem tools = new MenuItem("About Tools");
        MenuItem Rotate180 = new MenuItem("Rotate 180 Degrees");
        //This code is to add menu items to menu bar   
        menuBar.getMenus().addAll(menuFile, menuEdit, menuHelp);
        menuFile.getItems().addAll(openButton, closeButton, saveButton, saveasButton);
        menuEdit.getItems().addAll(Rotate180);
        menuHelp.getItems().addAll(helpButton, tools, release);

        helpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent ev) {
                Button exit = new Button("Exit");
                Label label = new Label("Paint Project Version: 6.0\n A Simmulation of Some of the Features from the Paint App");  //Information pertaining to the program
                label.setAlignment(Pos.TOP_CENTER);

                GridPane secondaryLayout = new GridPane();  //toggling the dimensions and everything in Grid Pane
                secondaryLayout.setAlignment(Pos.CENTER);
                secondaryLayout.add(exit, 1, 2);  //sets positioning of button and label
                secondaryLayout.add(label, 1, 1);
                secondaryLayout.setHgap(10);
                secondaryLayout.setVgap(10);
                secondaryLayout.setPadding(new Insets(25, 25, 25, 25));

                Scene secondScene = new Scene(secondaryLayout, NEW_WINDOW_WIDTH, NEW_WINDOW_HEIGHTH);

                Stage newWindow = new Stage();  //creates the new window (stage)
                newWindow.setTitle("CS 250 Paint Project");
                newWindow.setScene(secondScene);

                newWindow.setX(Primarystage.getX() + 200);  //sets dimensions of where the window will pop up
                newWindow.setY(Primarystage.getY() + 200);

                newWindow.show();

                exit.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        newWindow.close();  //closes the window
                    }
                });
            }
        });
        Rotate180.setOnAction((ActionEvent e) -> {
            // Create a WritableImage including the canvas' contents
            WritableImage wi = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
            PixelWriter pw = wi.getPixelWriter();
            Image io = sp.snapshot(null, null);
            PixelReader pixel = io.getPixelReader();
            for (int x = 0; x < wi.getWidth(); x++) {
                for (int y = 0; y < wi.getHeight(); y++) {
                    pw.setArgb((int) wi.getWidth() - x - 1, (int) wi.getHeight() - y - 1, pixel.getArgb(x, y));
                }
            }
            sp.resize(canvas.getWidth(), canvas.getHeight());
            gc.drawImage(wi, 0, 0, canvas.getWidth(), canvas.getHeight());
        });
        release.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Label releasenotes = new Label("Release Notes: \n"
                        + "\n"
                        + "VERSION: 6.0 [10/16/2019] \n"
                        + "\n"
                        + "NEW FEATURES: The program now has copy and paste capability. \n"
                        + "The ability to keep a log of everything selected tool and open/close in a log file. There is a hover tool \n "
                        + "that gives the description of each tool when the mouse is resting over that option. \n"
                        + "The ability to flip objects horizontally and vertically. \n"
                        + "\n"
                        + "VERSION: 5.0 [10/09/2019] \n"
                        + "\n"
                        + "NEW FEATURES: The program is now able to have a timer that allows auto save.\n"
                        + "The ability to save in an alternative file format with a warning alert pop up.\n"
                        + "There are three J unit tests that have all passed. Java Documentation has been added to the code.\n"                       
                        + "\n"
                        + "VERSION: 4.0 [09/28/2019] \n"
                        + "\n"
                        + "New Features: Drawing tool for freehand and straight line with width and color capability.\n"
                        + "Shape buttons added square, rectangle, ellipse, circle with fill capability.\n" 
                        + "Smart save alert added. The ability to open and save multiple files. A tool bar with all the buttons listed.\n"
                        + "Keyboard UI controls for save, open file, close and help.\n" 
                        + "Color grabber tool. Resize the canvas capability. The ability to zoom in and out.\n"
                        + "Redo and Undo buttons working on tool bar.\n"
                        + "\n"
                        + "VERSION: 3.0 [09/24/2019] \n"
                        + "\n"
                        + "NEW FEATURES: Drawing tool for freehand and straight line with width and color capability.\n "
                        + "Shape buttons added square, rectangle, ellipse, circle with fill capability.\n"
                        + "Smart save alert added. The ability to open and save multiple files.\n"
                        + "A tool bar with all the buttons listed. Keyboard UI controls for save, open file, close and help.\n"
                        + "Color grabber tool. Resize the canvas capability.\n"
                        + "The ability to zoom in and out. Redo and Undo buttons working on tool bar.\n"
                        + "\n"
                        + "VERSION: 2.0 [09/20/2019] \n"
                        + "\n"
                        + "NEW FEATURES: Save as ability, draw a line capability, canvas resize capability,\n"
                        + "scroll bars for width and height.\n"
                        + "\n"
                        + "VERSION: 1.0 [09/16/219] \n"
                        + "\n"
                        + "NEW FEATURES: Menu bar with the ability to open and close window. The option to save the file.");
                HBox thirdLayout = new HBox();
                thirdLayout.setPadding(new Insets(15, 12, 15, 12));
                thirdLayout.setSpacing(150);  //The spacing between the buttons
                thirdLayout.getChildren().add(releasenotes);

                releasenotes.setAlignment(Pos.CENTER);  //places the label in middle of hBoc

                ScrollPane releaseScroll = new ScrollPane();  //Creates ScrollPane
                releaseScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);  //Scrollers only show when the window squeezes the photo
                releaseScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
                releaseScroll.setContent(thirdLayout);  //adds hbox to scrollpane

                Scene releaseScene = new Scene(releaseScroll, NOTES_WINDOW_WIDTH, NOTES_WINDOW_HEIGHT);

                Stage releaseWindow = new Stage();  //creates the new window (stage)
                releaseWindow.setTitle("Release Notes");
                releaseWindow.setScene(releaseScene);

                releaseWindow.show();
            }
        });
        tools.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Label notes = new Label("Slider tool: Allows user to adjust the width of lines"
                        + "\n"
                        + "\n"
                        + "Color Picker: Allows user to pick designated color"
                        + "\n"
                        + "\n"
                        + "Draw Tool: Allows User to Free hand Draw"
                        + "\n"
                        + "\n"
                        + "Line Tool: Allows user to draw straight lines"
                        + "\n"
                        + "\n"
                        + "Polygon Tool: Allows user to choose the number of sides to draw a polygon"
                        + "\n"
                        + "\n"
                        + "Rectangle Tool: Allows user to draw a rectangle"
                        + "\n"
                        + "\n"
                        + "Square Tool: Allows user to draw a square"
                        + "\n"
                        + "\n"
                        + "Circle Tool: Allows user to draw a circle"
                        + "\n"
                        + "\n"
                        + "Ellipse Tool: Allows user to draw an oval"
                        + "\n"
                        + "\n"
                        + "Dropper Tool: Allows user to grab whatever color they clicked"
                        + "\n"
                        + "\n"
                        + "Eraser Tool: Allows user to erase anything they draw"
                        + "\n"
                        + "\n"
                        + "Text Tool: Allows user to put whatever text they type in wherever they want on canvas"
                        + "\n"
                        + "\n"
                        + "Redo Tool: Redoes any change to canvas the user makes"
                        + "\n"
                        + "\n"
                        + "Undo Tool: Undoes any changes to the canvas the user makes"
                        + "\n"
                        + "\n"
                        + "Moved Tool: Moves any selected portion to another place and leaves a void"
                        + "\n"
                        + "\n"
                        + "Selected Tool: Allows user to copy the selected portion of object"
                        + "\n"
                        + "\n"
                        + "Paste Tool: Allows user to paste the selected portion of object and doesn't leave a void");

                HBox thirdLayout = new HBox();
                thirdLayout.setPadding(new Insets(15, 12, 15, 12));
                thirdLayout.setSpacing(150);  //The spacing between the buttons
                thirdLayout.getChildren().add(notes);

                notes.setAlignment(Pos.CENTER);  //places the label in middle of hBoc

                ScrollPane toolsScroll = new ScrollPane();  //Creates ScrollPane
                toolsScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);  //Scrollers only show when the window squeezes the photo
                toolsScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
                toolsScroll.setContent(thirdLayout);  //adds hbox to scrollpane

                Scene toolsScene = new Scene(toolsScroll, TOOLS_WINDOW_WIDTH, TOOLS_WINDOW_HEIGHT);

                Stage releaseWindow = new Stage();  //creates the new window (stage)
                releaseWindow.setTitle("Tools");
                releaseWindow.setScene(toolsScene);

                releaseWindow.show();
            }
        });
        //Keyboard UI controls save/open/close/help.
        saveButton.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        openButton.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        closeButton.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN));
        helpButton.setAccelerator(new KeyCodeCombination(KeyCode.H, KeyCombination.CONTROL_DOWN));
        //This code is to activate the open button.
        openButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                //file chooser invoke an open dialog window to chosee a file. 
                FileChooser fileChooser = new FileChooser();
                selectedFile = fileChooser.showOpenDialog(null);
                if (selectedFile != null) {
                    Image image1 = new Image(selectedFile.toURI().toString());
                    sp.resize(image1.getWidth(), image1.getHeight());
                    canvas.setHeight(image1.getHeight());
                    canvas.setWidth(image1.getWidth());
                    iv.setImage(image1);
                } else {
                }
                Thread th = new Thread(new AutoSave());
                th.start();
            }
        });
        //This code is to activate the save button.
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (selectedFile != null) {
                    try {
                        ImageIO.write(SwingFXUtils.fromFXImage(sp.snapshot(null, null), null), "png", selectedFile);

                    } catch (IOException ex) {
                        Logger.getLogger(PaintSupreme.class.getName()).log(Level.SEVERE, null, ex);
                        System.out.println(ex.getMessage());
                    }
                }
            }
        });
        //This code is for the close button event.
        closeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                System.exit(0);
            }
        });//This code is for my save as button event.
        saveasButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save As");
                System.out.println(iv.getId());
                selectedFile = fileChooser.showSaveDialog(Primarystage);

                try {
                    if (!selectedFile.exists()) {
                        selectedFile.createNewFile();
                    }

                    ImageIO.write(SwingFXUtils.fromFXImage(sp.snapshot(null, null), null), "png", selectedFile);

                } catch (IOException ex) {
                    Logger.getLogger(PaintSupreme.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println(ex.getMessage());
                }
                Alert warning = new Alert(Alert.AlertType.CONFIRMATION);
                warning.setHeaderText("Warning!!! Image and Features/Data Loss May Happen!");
                ButtonType okay = new ButtonType("It's Okay!");
                warning.getButtonTypes().setAll(okay);
                warning.show();
            }
        });
        return menuBar;
    }
    //Auto Save Method

    public class AutoSave implements Runnable {
        @Override
        public void run() {
            boolean run = true;
            while (true) {
                try {
                    Thread.sleep(10000);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ImageIO.write(SwingFXUtils.fromFXImage(sp.snapshot(null, null), null), "png", selectedFile);
                            } catch (IOException ex) {
                                Logger.getLogger(PaintSupreme.class.getName()).log(Level.SEVERE, null, ex);
                                System.out.println(ex.getMessage());
                            }
                        }
                    });
                } catch (InterruptedException ex) {
                    Logger.getLogger(PaintSupreme.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    public class ToolThread implements Runnable {
        public long TimeStart;
        public long TimeEnd;
        String tool;
        @Override
        public void run() {
            tool = ToolLabel.getText();
            TimeStart = System.currentTimeMillis();
            String tool2 = ToolLabel.getText();

            while (tool2.equals(tool)) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(PaintSupreme.class.getName()).log(Level.SEVERE, null, ex);
                }
                tool2 = ToolLabel.getText();
            }
            final long time = System.currentTimeMillis() - TimeStart;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    logger.info(tool + ": " + time);
                }
            });
        }
    }
}
