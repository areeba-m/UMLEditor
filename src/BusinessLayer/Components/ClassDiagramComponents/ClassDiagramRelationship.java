package BusinessLayer.Components.ClassDiagramComponents;

import BusinessLayer.Components.UMLComponent;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

public class ClassDiagramRelationship extends UMLComponent{
    ClassBox classA;
    ClassBox classB;

    String type;//types: 1)association, composition, aggregation

    Point endPoint;

    boolean resizingStart = false;
    boolean resizingEnd = false;
    boolean rotating = false; // For tracking rotation state
    Point dragStartPoint;
    Point centerPoint;

    //for inheritance only
    ClassBox parent;
    ArrayList<ClassBox> child;

    public ClassDiagramRelationship(Point p1, Point p2)
    {
        super();

        setPreferredSize(new Dimension(100, 100));
        point = new Point();
        endPoint = new Point();
        point.setLocation(p1.getX(), p1.getY());
        endPoint.setLocation(p2.getX(), p2.getY());
        isGridPanel = false;
        centerPoint = new Point((point.x + endPoint.x) / 2, (point.y + endPoint.y) / 2); // Initial midpoint

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (isNearResizeArea(e.getPoint(), point)) {
                    resizingStart = true;
                    dragStartPoint = e.getPoint();
                } else if (isNearResizeArea(e.getPoint(), endPoint)) {
                    resizingEnd = true;
                    dragStartPoint = e.getPoint();
                } else if (isNearRotateArea(e.getPoint())) {
                    rotating = true;
                    dragStartPoint = e.getPoint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                resizingStart = false;
                resizingEnd = false;
                rotating = false;
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (resizingStart) {
                    int deltaX = e.getX() - dragStartPoint.x;
                    int deltaY = e.getY() - dragStartPoint.y;
                    point.translate(deltaX, deltaY);
                    dragStartPoint = e.getPoint();
                    repaint();
                } else if (resizingEnd) {
                    int deltaX = e.getX() - dragStartPoint.x;
                    int deltaY = e.getY() - dragStartPoint.y;
                    endPoint.translate(deltaX, deltaY);
                    dragStartPoint = e.getPoint();
                    repaint();
                } else if (rotating) {
                    // Calculate the angle difference for rotation
                    double angle = Math.atan2(e.getY() - centerPoint.y, e.getX() - centerPoint.x) -
                            Math.atan2(dragStartPoint.y - centerPoint.y, dragStartPoint.x - centerPoint.x);
                    rotateLine(angle);
                    dragStartPoint = e.getPoint();
                    repaint();
                }
            }
        });
    }

    private boolean isNearResizeArea(Point mousePoint, Point linePoint) {
        int tolerance = 10; // Define the range near the point where resizing can happen
        return Math.abs(mousePoint.x - linePoint.x) <= tolerance && Math.abs(mousePoint.y - linePoint.y) <= tolerance;
    }

    private boolean isNearRotateArea(Point mousePoint) {
        int tolerance = 15; // Define the range around the center point where rotation can happen
        return Math.abs(mousePoint.x - centerPoint.x) <= tolerance && Math.abs(mousePoint.y - centerPoint.y) <= tolerance;
    }

    private void rotateLine(double angle) {
        // Rotate both points around the center point
        point = rotatePointAround(centerPoint.x, centerPoint.y, point, angle);
        endPoint = rotatePointAround(centerPoint.x, centerPoint.y, endPoint, angle);
        repaint();
    }

    private Point rotatePointAround(int centerX, int centerY, Point p, double angle) {
        // Translate point to origin (pivot at center)
        double x = p.x - centerX;
        double y = p.y - centerY;

        // Rotate the point around the origin
        double newX = x * Math.cos(angle) - y * Math.sin(angle);
        double newY = x * Math.sin(angle) + y * Math.cos(angle);

        // Translate back
        return new Point((int) (newX + centerX), (int) (newY + centerY));
    }

    public void setType(String type)
    {
        this.type = type;
    }

    @Override
    public void updateFromTextArea() {

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Set the color and stroke for the line
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));

        // Draw the main line
        g2d.drawLine(point.x, point.y, endPoint.x, endPoint.y);

        // Draw the relationship-specific symbols at the end
        if (type.equals("aggregation")) {
            drawHollowDiamond(g2d, endPoint.x+8, endPoint.y);
        } else if (type.equals("composition")) {
            drawFilledDiamond(g2d, endPoint.x+8, endPoint.y);
        }else if (type.equals("inheritence")) {
            drawUnfilledTriangle(g2d, endPoint.x+13, endPoint.y);
        }
    }

    // Method to draw a hollow diamond (aggregation)
    private void drawHollowDiamond(Graphics2D g2d, int x, int y) {
        int size = 10;
        Path2D.Double diamond = new Path2D.Double();
        diamond.moveTo(x, y - size); // top
        diamond.lineTo(x + size, y); // right
        diamond.lineTo(x, y + size); // bottom
        diamond.lineTo(x - size, y); // left
        diamond.closePath();
        g2d.setColor(Color.BLACK);
        g2d.draw(diamond);  // Hollow diamond (just outline)
    }

    // Method to draw a filled diamond (composition)
    private void drawFilledDiamond(Graphics2D g2d, int x, int y) {
        int size = 10;
        Path2D.Double diamond = new Path2D.Double();
        diamond.moveTo(x, y - size); // top
        diamond.lineTo(x + size, y); // right
        diamond.lineTo(x, y + size); // bottom
        diamond.lineTo(x - size, y); // left
        diamond.closePath();
        g2d.setColor(Color.BLACK);
        g2d.fill(diamond);  // Filled diamond
    }

    // Method to draw an unfilled diamond (inheritance)
    private void drawUnfilledDiamond(Graphics2D g2d, int x, int y) {
        int size = 10;
        Path2D.Double diamond = new Path2D.Double();
        diamond.moveTo(x, y - size); // top
        diamond.lineTo(x + size, y); // right
        diamond.lineTo(x, y + size); // bottom
        diamond.lineTo(x - size, y); // left
        diamond.closePath();
        g2d.setColor(Color.BLACK);
        g2d.draw(diamond);  // Unfilled diamond (just outline)
    }
    private void drawUnfilledTriangle(Graphics2D g2d, int x, int y) {
        int size = 15; // Increased the size of the triangle
        double angle = Math.atan2(point.y - y, point.x - x); // Angle of the line

        // Calculate the triangle's three points based on the endpoint and angle
        int x1 = x + (int) (size * Math.cos(angle - Math.PI / 6));
        int y1 = y + (int) (size * Math.sin(angle - Math.PI / 6));
        int x2 = x + (int) (size * Math.cos(angle + Math.PI / 6));
        int y2 = y + (int) (size * Math.sin(angle + Math.PI / 6));

        // Create the triangle shape
        Path2D.Double triangle = new Path2D.Double();
        triangle.moveTo(x, y);
        triangle.lineTo(x1, y1);
        triangle.lineTo(x2, y2);
        triangle.closePath();

        g2d.setColor(Color.BLACK);
        g2d.draw(triangle);  // Outline of triangle
    }

    @Override
    public void draw(Graphics g) {

    }

    @Override
    public void setMethods(List<Object> methods) {

    }

    @Override
    public void setAttributes(List<Object> attributes) {

    }
}