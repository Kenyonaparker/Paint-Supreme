/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paintsupreme;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

/**
 *
 * @author Kenyona
 */
public class Polygons extends Stages {

    private Text actionStatus;
    private int polygonSides;
    private Polygon poly;
    private final int POLY_WINDOW_WIDTH = 450;
    private final int POLY_WINDOW_HEIGHT = 150;
    private final int NEW_WINDOW_WIDTH = 400;
    private final int NEW_WINDOW_HEIGHTH = 150;
    private final double NOTES_WINDOW_WIDTH = 750;
    private final double NOTES_WINDOW_HEIGHT = 750;
    private final double TOOLS_WINDOW_WIDTH = 750;
    private final double TOOLS_WINDOW_HEIGHT = 550;
    private double startX;
    private double startY;
    private double mouseClickX;
    private double mouseClickY;
    private double xClick;
    private double yClick;
    private Pane p;
    private GraphicsContext gc;
    private ColorPicker colorPicker;

    /**
     * This method draws the polygon onto the canvas
     *
     * @param canvas
     * @param numPoints
     * @param xClickPoint
     * @param yClickPoint
     * @param secondClickX
     * @param secondClickY
     * @param fill
     */
    public void drawPolygon(Canvas canvas, int numPoints, double xClickPoint, double yClickPoint, double secondClickX, double secondClickY, Boolean fill) {
        double[] xPoints = new double[numPoints + 1];
        double[] yPoints = new double[numPoints + 1];

        xPoints[0] = secondClickX;
        yPoints[0] = secondClickY;

        final double angleStep = Math.PI * 2 / numPoints;
        double radius = Math.sqrt(((secondClickX - xClickPoint) * (secondClickX - xClickPoint)) + ((secondClickY - yClickPoint) * (secondClickY - yClickPoint)));
        double angle = Math.atan2(secondClickY - yClickPoint, secondClickX - xClickPoint);

        for (int i = 0; i < numPoints + 1; i++, angle += angleStep) {
            xPoints[i] = Math.cos(angle) * radius + xClickPoint;
            yPoints[i] = Math.sin(angle) * radius + yClickPoint;
        }

        if (fill) {
            canvas.getGraphicsContext2D().fillPolygon(xPoints, yPoints, numPoints + 1);
        }

        canvas.getGraphicsContext2D().strokePolygon(xPoints, yPoints, numPoints + 1);

    }
    ////\\\\\This code creates the real polygon on the canvas////\\\\

    /**
     * This polygon method located on tool bar with user selection for number of
     * sides
     *
     * @param canvas
     * @param numPoints
     * @param xClickPoint
     * @param yClickPoint
     * @param secondClickX
     * @param secondClickY
     * @param fill
     * @return
     */
    public Polygon tempPolygon(Canvas canvas, int numPoints, double xClickPoint, double yClickPoint, double secondClickX, double secondClickY, Boolean fill) {
        Polygon tempPolygon = new Polygon();
        tempPolygon.getPoints().addAll(secondClickX, secondClickY);
        p.getChildren().add(tempPolygon);
        final double angleStep = Math.PI * 2 / numPoints;
        double radius = Math.sqrt(((secondClickX - xClickPoint) * (secondClickX - xClickPoint)) + ((secondClickY - yClickPoint) * (secondClickY - yClickPoint)));
        double angle = Math.atan2(secondClickY - yClickPoint, secondClickX - xClickPoint);

        for (int i = 0; i < numPoints; i++, angle += angleStep) {
            tempPolygon.getPoints().addAll(Math.cos(angle) * radius + xClickPoint, Math.sin(angle) * radius + yClickPoint);
        }

        tempPolygon.setStroke(colorPicker.getValue());
        tempPolygon.setStrokeWidth(gc.getLineWidth());
        if (fill) {
            tempPolygon.setFill(colorPicker.getValue());
        } else {
            tempPolygon.setFill(null);
        }

        return tempPolygon;//draws the temp Polygon so user can see the polygon
    }
}
