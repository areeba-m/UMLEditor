package BusinessLayer.Components.ClassDiagramComponents;

import BusinessLayer.Components.UMLComponent;
import BusinessLayer.Diagrams.UMLDiagram;

import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

public class Inheritence extends UMLComponent {
    ClassBox parent;
    ArrayList<ClassBox> child;
    Point endPoint;

    public Inheritence(Point p1, Point p2)
    {
        super();
        point = new Point();
        endPoint = new Point();
        point.setLocation(p1.getX(), p1.getY());
        endPoint.setLocation(p2.getX(), p2.getY());
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Set the color and stroke for the line
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));

        // Draw the main inheritance line
        g2d.drawLine(point.x, point.y, endPoint.x, endPoint.y);

        // Draw the unfilled triangle (inheritance)
        drawUnfilledTriangle(g2d, endPoint.x+13, endPoint.y);
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


    public void setParent(ClassBox classP)
    {

    }
    public void addChild(ClassBox classC)
    {

    }
    public ClassBox getParent()
    {
        return null;
    }
    public ArrayList<ClassBox> getChild()
    {
        return null;
    }

    @Override
    public void updateFromTextArea() {

    }

    public void draw(Graphics g){

    }

    @Override
    public void setMethods(List<Object> methods) {

    }

    @Override
    public void setAttributes(List<Object> attributes) {

    }
}