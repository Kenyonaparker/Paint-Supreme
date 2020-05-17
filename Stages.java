/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paintsupreme;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 *
 * @author Kenyona
 */
public class Stages extends Application {

    File selectedFile;
    FileChooser fileChooser;
    StackPane sp;
    Canvas canvas;
    ImageView iv;
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
    TextArea text;
    Ellipse ellipse;
    Ellipse elps;
    Circle circ;
    Line line;
    Slider wid;
    Text ToolLabel;
    Text TOOL;

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

    @Override
    public void start(Stage Primarystage) {

        VBox v = new VBox();

        Scene scene = new Scene(new VBox(), 900, 650);

        //You must have this iv object created to be able to veiw your image once selected. 
        iv = new ImageView();
        //These lines are necessary for your canvas to be displayed.
        canvas = new Canvas(900, 650);
        ImageView view = new ImageView();

        gc = canvas.getGraphicsContext2D();
        //This pane is created for my rectangle for my move to work. Then it was added to my stackpane.
        p = new Pane();

        //StackPane has my scroll bar and my image viewer and canvas 
        sp = new StackPane(iv, p, canvas, view);
        ScrollPane s = new ScrollPane();
        s.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        s.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        s.setContent(sp);
        //There needs to be a call made to create the bars
        Bars tool = new Bars();

        MenuBar menuBar = tool.menuBar(scene, Primarystage, sp, iv, canvas, gc);
        v.getChildren().addAll(menuBar);
        ((VBox) scene.getRoot()).getChildren().addAll(menuBar, s);

        //This color picker is for my fill of my shapes.
        cpLine = new ColorPicker(Color.BLACK);
        cpFill = new ColorPicker(Color.TRANSPARENT);
        colorPicker = new ColorPicker(Color.BLACK);

        ToolBar createtoolBar = tool.CreateToolBar(scene, Primarystage, gc);
        ToolBar secondtoolBar = tool.SecondToolBar(scene, Primarystage, canvas);

        wid = (Slider) createtoolBar.getItems().get(0);
        colorPicker = (ColorPicker) createtoolBar.getItems().get(1);
        linebtn = (ToggleButton) createtoolBar.getItems().get(2);
        drawbtn = (ToggleButton) createtoolBar.getItems().get(3);
        eraserbtn = (ToggleButton) createtoolBar.getItems().get(4);
        rectbtn = (ToggleButton) createtoolBar.getItems().get(5);
        circlebtn = (ToggleButton) createtoolBar.getItems().get(6);
        ellipsebtn = (ToggleButton) createtoolBar.getItems().get(7);
        polygon = (ToggleButton) createtoolBar.getItems().get(8);
        square = (ToggleButton) createtoolBar.getItems().get(9);

        textbtn = (ToggleButton) secondtoolBar.getItems().get(0);
        zoomin = (ToggleButton) secondtoolBar.getItems().get(1);
        zoomout = (ToggleButton) secondtoolBar.getItems().get(2);
        dropper = (ToggleButton) secondtoolBar.getItems().get(3);
        selected = (ToggleButton) secondtoolBar.getItems().get(4);
        moved = (ToggleButton) secondtoolBar.getItems().get(5);
        paste = (ToggleButton) secondtoolBar.getItems().get(6);
        undo = (Button) secondtoolBar.getItems().get(7);
        redo = (Button) secondtoolBar.getItems().get(8);
        TOOL = (Text) secondtoolBar.getItems().get(9);
        ToolLabel = (Text) secondtoolBar.getItems().get(10);
        text = (TextArea) secondtoolBar.getItems().get(11);

        //This is where my shape coding begins
        line = new Line();
        rect = new Rectangle();
        circ = new Circle();
        elps = new Ellipse();

        ((VBox) scene.getRoot()).getChildren().addAll(createtoolBar, secondtoolBar);
        //hover tool method for the buttons on the toolbars
        linebtn.setTooltip(new Tooltip("It draws a line"));
        drawbtn.setTooltip(new Tooltip("It makes a hand drawn line"));
        eraserbtn.setTooltip(new Tooltip("It erases anything that is drawn or uploaded"));
        rectbtn.setTooltip(new Tooltip("It draws a rectangle"));
        circlebtn.setTooltip(new Tooltip("It draws a circle"));
        ellipsebtn.setTooltip(new Tooltip("It draws an ellipse"));
        polygon.setTooltip(new Tooltip("It draws a polygon"));
        square.setTooltip(new Tooltip("It draws a square"));
        textbtn.setTooltip(new Tooltip("This button opens an area for your to type your text into"));
        zoomin.setTooltip(new Tooltip("This button zooms in onto the object"));
        zoomout.setTooltip(new Tooltip("This button zooms out of the object"));
        dropper.setTooltip(new Tooltip("This button extracts the color pixel from this area"));
        selected.setTooltip(new Tooltip("This button draws a square around the area that you want to select"));
        moved.setTooltip(new Tooltip("This button will move the selected portion of the canvas"));
        undo.setTooltip(new Tooltip("This button will undo any action"));
        redo.setTooltip(new Tooltip("This button will redo any action"));

        //This polygonclass was created in order to call on the polygon methods
        Polygons polygonclass = new Polygons();
        /**
         * All of the Mouse Pressed Events This code is the events for each
         * button when the mouse is pressed
         */
        canvas.setOnMousePressed(e -> {
            if (polygon.isSelected()) {
                startX = e.getX();
                startY = e.getY();
                gc.beginPath();
                startX = e.getX();  //variables to keep track of first click x
                startY = e.getY();  //variables to keep track of first click y
                gc.moveTo(e.getX(), e.getY());  //sets starting point of line
                gc.setStroke(colorPicker.getValue());  //Gets the color chosen by the user for the stroke
                gc.setLineWidth(gc.getLineWidth());
                
            }
            if (linebtn.isSelected()) {
                gc.setStroke(cpLine.getValue());
                gc.setLineWidth(wid.getValue());
                line.setStartX(e.getX());
                line.setStartY(e.getY());
            } else if (drawbtn.isSelected()) {
                gc.setStroke(cpLine.getValue());
                gc.setLineWidth(wid.getValue());
                gc.beginPath();
                gc.lineTo(e.getX(), e.getY());
            } else if (dropper.isSelected()) {
                Image dropperImage = sp.snapshot(null, null);
                PixelReader pr = dropperImage.getPixelReader();
                Color color = pr.getColor((int) e.getX(), (int) e.getY());
                colorPicker.setValue(color);
                gc.setStroke(color);
            } else if (eraserbtn.isSelected()) {
                double lineWidth = gc.getLineWidth();
                gc.clearRect(e.getX() - lineWidth / 2, e.getY() - lineWidth / 2, lineWidth, lineWidth);
            } else if (rectbtn.isSelected()) {
                gc.setStroke(cpLine.getValue());
                gc.setFill(cpFill.getValue());
                rect.setX(e.getX());
                rect.setY(e.getY());
            } else if (circlebtn.isSelected()) {
                gc.setStroke(cpLine.getValue());
                gc.setFill(cpFill.getValue());
                circ.setCenterX(e.getX());
                circ.setCenterY(e.getY());
            } else if (ellipsebtn.isSelected()) {
                gc.setStroke(cpLine.getValue());
                gc.setFill(cpFill.getValue());
                elps.setCenterX(e.getX());
                elps.setCenterY(e.getY());
            } else if (textbtn.isSelected()) {
                gc.setLineWidth(1);
                gc.setFont(Font.font(wid.getValue()));
                gc.setStroke(cpLine.getValue());
                gc.setFill(cpFill.getValue());
                gc.fillText(text.getText(), e.getX(), e.getY());
                gc.strokeText(text.getText(), e.getX(), e.getY());
            } else if (selected.isSelected()) {
                Rect = new Rectangle(e.getX(), e.getY(), 0, 0);  //creates rectangle
                Rect.setStroke(Color.BLACK);
                Rect.setFill(Color.TRANSPARENT);
                Rect.setStrokeWidth(gc.getLineWidth());
                p.getChildren().add(Rect);  //adds rectangle to drawing pane
                gc.beginPath();
                startX = e.getX();  //variables to keep track of first click x
                startY = e.getY();  //variables to keep track of first click y
            } else if (moved.isSelected()) {
                gc.beginPath();
                Paint tempColor = gc.getFill();
                actionsU.push(sp.snapshot(null, null));
                gc.setFill(Color.WHITE);
                gc.fillRect(Rect.getX(), Rect.getY(), Rect.getWidth(), Rect.getHeight());
                gc.setFill(tempColor);
                gc.closePath();
                Image selectedImage = (actionsU.peek());
                PixelReader pr = selectedImage.getPixelReader();
                WritableImage newImage = new WritableImage(pr, (int) Rect.getX(), (int) Rect.getY(), (int) Rect.getWidth(), (int) Rect.getHeight());
                cropImage = new ImageView(newImage);
                cropImage.setX(0);
                cropImage.setY(0);
                p.getChildren().add(cropImage);
                gc.setFill(Color.WHITE);
            } else if (paste.isSelected()) {
                gc.beginPath();
                actionsU.push(sp.snapshot(null, null));
                gc.closePath();
                Image selectedImage = (actionsU.peek());
                PixelReader pr = selectedImage.getPixelReader();
                WritableImage newImage = new WritableImage(pr, (int) Rect.getX(), (int) Rect.getY(), (int) Rect.getWidth(), (int) Rect.getHeight());
                cropImage = new ImageView(newImage);
                cropImage.setX(0);
                cropImage.setY(0);
                p.getChildren().add(cropImage);
            } else if (square.isSelected()) {
                rect.setStroke(colorPicker.getValue());
                rect.setStrokeWidth(gc.getLineWidth());
                rect.setFill(colorPicker.getValue());
                p.getChildren().add(rect);  //adds rectangle to drawing pane
                gc.beginPath();
                startX = e.getX();  //variables to keep track of first click x
                startY = e.getY();  //variables to keep track of first click y
                gc.moveTo(e.getX(), e.getY());  //sets starting point of line
                gc.setStroke(colorPicker.getValue());  //Gets the color chosen by the user for the stroke
                gc.setLineWidth(gc.getLineWidth());
            }
        });
        /*All of the Mouse Dragged Events
        *This codes is for the events for each button when the mouse is dragged aross the canvas
         */
        canvas.setOnMouseDragged(e -> {
            if (polygon.isSelected()) {
                {
                    polygonclass.tempPolygon(canvas, polygonSides, startX, startY, e.getX(), e.getY(), true);
                    //creates the temp polygon
                }
            }
            if (drawbtn.isSelected()) {
                gc.setLineWidth(wid.getValue());
                gc.setStroke(cpLine.getValue());
                gc.setFill(cpLine.getValue());
                gc.lineTo(e.getX(), e.getY());
                gc.stroke();
            } else if (eraserbtn.isSelected()) {
                double lineWidth = gc.getLineWidth();
                gc.clearRect(e.getX() - lineWidth / 2, e.getY() - lineWidth / 2, lineWidth, lineWidth);
            } else if (selected.isSelected()) {
                Rect.setWidth(e.getX() - startX);  //follows the mouse for user to draw shape on a pane
                Rect.setHeight(e.getY() - startY);
            } else if (moved.isSelected()) {
                cropImage.setX(e.getX());
                cropImage.setY(e.getY());
            } else if (paste.isSelected()) {
                cropImage.setX(e.getX());
                cropImage.setY(e.getY());
            } else if (square.isSelected()) {
                rect.setWidth(e.getY() - startY);  //follows the mouse for user to draw shape on a pane
                rect.setHeight(e.getY() - startY);
            }
        });
        /**
         * All of the Mouse Released Events This code if for all the events for
         * each button when the mouse is released after being dragged on the
         * canvas
         */
        canvas.setOnMouseReleased(e -> {

            if (polygon.isSelected()) {
                p.getChildren().remove(poly);
                gc.setFill(colorPicker.getValue());  //sets fill color to colorpicker
                gc.stroke();
                gc.fill();  //fills the square shape
                gc.closePath();
                polygonclass.drawPolygon(canvas, polygonSides, startX, startY, e.getX(), e.getY(), true);
                //creates the polygon
                polygon.setSelected(false);  //Diselects button
            }

            gc.setStroke(colorPicker.getValue());
            gc.setFill(colorPicker.getValue());
            if (linebtn.isSelected()) {
                line.setEndX(e.getX());
                line.setEndY(e.getY());
                gc.strokeLine(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());

                tool.pushToHistory(new Line(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY()));

            } else if (drawbtn.isSelected()) {
                gc.lineTo(e.getX(), e.getY());
                gc.stroke();
                gc.closePath();

            } else if (eraserbtn.isSelected()) {
                double lineWidth = gc.getLineWidth();
                gc.clearRect(e.getX() - lineWidth / 2, e.getY() - lineWidth / 2, lineWidth, lineWidth);
            } else if (rectbtn.isSelected()) {
                rect.setWidth(Math.abs((e.getX() - rect.getX())));
                rect.setHeight(Math.abs((e.getY() - rect.getY())));
                //rect.setX((rect.getX() > e.getX()) ? e.getX(): rect.getX());
                if (rect.getX() > e.getX()) {
                    rect.setX(e.getX());
                }
                //rect.setY((rect.getY() > e.getY()) ? e.getY(): rect.getY());
                if (rect.getY() > e.getY()) {
                    rect.setY(e.getY());
                }

                gc.fillRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
                gc.strokeRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());

                tool.pushToHistory(new Rectangle(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight()));
            } else if (circlebtn.isSelected()) {
                circ.setRadius((Math.abs(e.getX() - circ.getCenterX()) + Math.abs(e.getY() - circ.getCenterY())) / 2);

                if (circ.getCenterX() > e.getX()) {
                    circ.setCenterX(e.getX());
                }
                if (circ.getCenterY() > e.getY()) {
                    circ.setCenterY(e.getY());
                }

                gc.fillOval(circ.getCenterX(), circ.getCenterY(), circ.getRadius(), circ.getRadius());
                gc.strokeOval(circ.getCenterX(), circ.getCenterY(), circ.getRadius(), circ.getRadius());

                tool.pushToHistory(new Circle(circ.getCenterX(), circ.getCenterY(), circ.getRadius()));
            } else if (ellipsebtn.isSelected()) {
                elps.setRadiusX(Math.abs(e.getX() - elps.getCenterX()));
                elps.setRadiusY(Math.abs(e.getY() - elps.getCenterY()));

                if (elps.getCenterX() > e.getX()) {
                    elps.setCenterX(e.getX());
                }
                if (elps.getCenterY() > e.getY()) {
                    elps.setCenterY(e.getY());
                }
                gc.strokeOval(elps.getCenterX(), elps.getCenterY(), elps.getRadiusX(), elps.getRadiusY());
                gc.fillOval(elps.getCenterX(), elps.getCenterY(), elps.getRadiusX(), elps.getRadiusY());

                tool.pushToHistory(new Ellipse(elps.getCenterX(), elps.getCenterY(), elps.getRadiusX(), elps.getRadiusY()));

            } else if (selected.isSelected()) {
                int width = (int) (e.getX() - startX);
                int height = (int) (e.getY() - startY);
                p.getChildren().remove(Rect);
                gc.closePath();
            } else if (moved.isSelected()) {
                p.getChildren().remove(cropImage);
                gc.drawImage(cropImage.getImage(), Rect.getWidth(), Rect.getHeight());
            } else if (paste.isSelected()) {
                p.getChildren().remove(cropImage);
                gc.drawImage(cropImage.getImage(), Rect.getWidth(), Rect.getHeight());
            } else if (square.isSelected()) {
                p.getChildren().remove(rect);
                gc.setFill(colorPicker.getValue());  //sets fill color to colorpicker
                gc.rect(startX, startY, e.getY() - startY, e.getY() - startY);  //creates a square on canvas by taking the difference between y-value of the unclick and the y-value click and uses thpse as height and width
                gc.stroke();
                gc.fill();  //fills the square shape
                gc.closePath();
            }

            if (undoHistory.size() > 0) {
                redoHistory.clear();
                Shape lastUndo = undoHistory.lastElement();
                lastUndo.setFill(gc.getFill());
                lastUndo.setStroke(gc.getStroke());
                lastUndo.setStrokeWidth(gc.getLineWidth());
            }
        });
        /// \\\color picker////\\\\
        cpLine.setOnAction(e -> {
            gc.setStroke(cpLine.getValue());
        });
        cpFill.setOnAction(e -> {
            gc.setFill(cpFill.getValue());
        });
        /**
         * Polygon number of sides picked by user section
         */
        polygon.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                Button enternum = new Button("Enter");
                //creates Label for textField
                Label sides = new Label("Number of Sides: ");
                //creates textfield for user to input their number
                TextField input = new TextField();
                input.setPromptText("Number of Sides");
                tool.ToolLabel.setText("polygon");
                GridPane grid = new GridPane();
                grid.setAlignment(Pos.CENTER);
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(25, 25, 25, 25));

                grid.add(sides, 0, 1);  //adds label to grid
                grid.add(input, 1, 1);  //adds textfield to grid
                grid.add(enternum, 0, 2);  //adds button to grid
                //adds the grid to the window
                Scene polyScene = new Scene(grid, POLY_WINDOW_WIDTH, POLY_WINDOW_HEIGHT);
                //creates the new window (stage)
                Stage polyWindow = new Stage();

                polyWindow.setTitle("Polygon Sides");
                polyWindow.setScene(polyScene);
                polyWindow.show();

                enternum.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent e) {
                        //sets universal int variable polygon sides to users input
                        polygonSides = Integer.parseInt(input.getText());
                        polyWindow.close();
                    }
                });
            }
        });

        //This is the call for smart save that includes my primarystage and imageviewer
        Primarystage.setOnCloseRequest(WindowEvent -> {
            smartSave(WindowEvent, Primarystage, iv);
        });
        //This is always last and it makes sure the window is displayed.
        Primarystage.setTitle("Menu");
        Primarystage.setScene(scene);
        Primarystage.show();
    }

    /**
     * Smart save method shows an alert to user if they close before saving
     * changes
     *
     * @param e
     * @param Primarystage
     * @param iv
     */
    public void smartSave(Event e, Stage Primarystage, ImageView iv) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Are you sure you want to Close without Saving?");
        ButtonType dontsave = new ButtonType("Don't Save");
        ButtonType save = new ButtonType("Save");
        ButtonType cancel = new ButtonType("Cancel");
        alert.getButtonTypes().setAll(dontsave, save, cancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == dontsave) {
            System.exit(0);
        }
        if (result.get() == save) {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("All Images", "*.*");
            FileChooser.ExtensionFilter extfilterJPG = new FileChooser.ExtensionFilter("JPG", "*.jpg");
            FileChooser.ExtensionFilter extFilerPNG = new FileChooser.ExtensionFilter("PNG", "*.png");
            fileChooser.setTitle("Save As");
            System.out.println(iv.getId());
            selectedFile = fileChooser.showSaveDialog(Primarystage);

            try {
                if (!selectedFile.exists()) {
                    selectedFile.createNewFile();
                }

                ImageIO.write(SwingFXUtils.fromFXImage(iv.getImage(), null), "png", selectedFile);

            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
        if (result.get() == cancel) {
            e.consume();
        }
    }

    //Auto Save Method
    public class AutoSave implements Runnable {

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

}
