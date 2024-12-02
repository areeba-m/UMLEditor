package BusinessLayer.Components.ClassDiagramComponents;

import BusinessLayer.Components.UMLComponent;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

public class ClassDiagramRelationship extends UMLComponent{

    Point centerPoint;
    Point endPoint;

    private UMLComponent from;
    private UMLComponent to;

    //for inheritance only
    ClassBox parent;
    ArrayList<ClassBox> child;

    public ClassDiagramRelationship(UMLComponent from, UMLComponent to, String name)
    {
        super();
        this.from = from;
        this.to = to;
        this.name = name;

        // Attach listeners to update bounds when 'from' or 'to' move
        if (from != null && to != null) {

            if(from instanceof ClassBox && to instanceof ClassBox) {
                ((ClassBox) from).addRelationship(this);
                ((ClassBox) to).addRelationship(this);
            }

            from.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentMoved(ComponentEvent e) {
                    updateBounds();
                    repaint();
                }
            });

            to.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentMoved(ComponentEvent e) {
                    updateBounds();
                    repaint();
                }
            });

            // **Force initial bounds calculation**
            updateBounds();
            repaint(); // Ensure the component is drawn immediately
        }
    }

    @Override
    public void updateFromTextArea() {

    }

    @Override
    public void draw(Graphics g) {

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Set the color and stroke for the line
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));

        // Draw the main line
        point = calculateConnectionPoint(from, to);
        endPoint = calculateConnectionPoint(to, from);

        g2d.drawLine(point.x, point.y, endPoint.x, endPoint.y);

        // Draw the relationship-specific symbols at the end
        if (name.equalsIgnoreCase("aggregation")) {
            drawHollowDiamond(g2d, endPoint.x + 8, endPoint.y);
        } else if (name.equalsIgnoreCase("composition")) {
            drawFilledDiamond(g2d, endPoint.x + 8, endPoint.y);
        } else if (name.equalsIgnoreCase("inheritance")) {
            drawUnfilledTriangle(g2d, endPoint.x + 13, endPoint.y);
        }

        if (isSelected()) {
            g2d.setColor(Color.BLUE);
            g2d.fillOval(point.x - 5, point.y - 5, 10, 10);
            g2d.fillOval(endPoint.x - 5, endPoint.y - 5, 10, 10);
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

    private Point calculateConnectionPoint(UMLComponent component, UMLComponent otherComponent) {
        if (component == null || otherComponent == null) {
            return null;
        }

        Rectangle bounds = component.getBounds();
        Rectangle otherBounds = otherComponent.getBounds();

        // Center of this component
        int centerX = bounds.x + bounds.width / 2;
        int centerY = bounds.y + bounds.height / 2;

        // Center of the other component
        int otherCenterX = otherBounds.x + otherBounds.width / 2;
        int otherCenterY = otherBounds.y + otherBounds.height / 2;

        // Direction vector from this component's center to the other component's center
        double dx = otherCenterX - centerX;
        double dy = otherCenterY - centerY;

        // Find the intersection of the line with the bounds
        double scaleX = dx == 0 ? Double.MAX_VALUE : (dx > 0 ? bounds.width / 2.0 : -bounds.width / 2.0) / dx;
        double scaleY = dy == 0 ? Double.MAX_VALUE : (dy > 0 ? bounds.height / 2.0 : -bounds.height / 2.0) / dy;
        double scale = Math.min(scaleX, scaleY);

        // Calculate the intersection point
        int edgeX = (int) (centerX + scale * dx);
        int edgeY = (int) (centerY + scale * dy);

        // Transform the point relative to this component's bounds
        return new Point(edgeX - getX(), edgeY - getY());
    }

    private void updateBounds() {
        if (from == null || to == null) {
            int x = Math.min(point.x, endPoint.x) - 10; // Add padding for x
            int y = Math.min(point.y, endPoint.y) - 10; // Add padding for y
            int width = Math.abs(endPoint.x - point.x) + 20; // Add padding for width
            int height = Math.abs(endPoint.y - point.y) + 20; // Add padding for height

            setBounds(10, 10, width, height);
            System.out.println("default bounds: " + getBounds());
            return;
        }

        Rectangle fromBounds = from.getBounds();
        Rectangle toBounds = to.getBounds();

        // Calculate the smallest bounding box containing both components
        int x = Math.min(fromBounds.x, toBounds.x);
        int y = Math.min(fromBounds.y, toBounds.y);
        int width = Math.max(fromBounds.x + fromBounds.width, toBounds.x + toBounds.width) - x;
        int height = Math.max(fromBounds.y + fromBounds.height, toBounds.y + toBounds.height) - y;

        // Update this component's bounds
        setBounds(x, y, width, height);
    }

    public UMLComponent getFrom() {
        return from;
    }

    public UMLComponent getTo() {
        return to;
    }

    public void removeFromAndTo(){
        ((ClassBox)to).removeRelationship(this);
        ((ClassBox)from).removeRelationship(this);
        revalidate();
        repaint();
    }

    @Override
    public void setMethods(List<Object> methods) {

    }

    @Override
    public void setAttributes(List<Object> attributes) {

    }
}