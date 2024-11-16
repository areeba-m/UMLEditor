package BusinessLayer.Components.ClassDiagramComponents;

import BusinessLayer.Components.UMLComponent;

import java.awt.*;
import java.awt.geom.Path2D;

public class ClassDiagramRelationship extends UMLComponent {
    ClassBox classA;
    ClassBox classB;
    String name;//name of relationship example "uses", will be empty for composition and aggregation
    String type;//types: 1)association, composition, aggregation
    Point endPoint;

    public ClassDiagramRelationship(Point p1, Point p2)
    {
        point = new Point();
        endPoint = new Point();
        point.setLocation(p1.getX(), p1.getY());
        endPoint.setLocation(p2.getX(), p2.getY());
    }

    public void setType(String type)
    {
        this.type = type;
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
    @Override
    public void draw(Graphics g) {

    }
}