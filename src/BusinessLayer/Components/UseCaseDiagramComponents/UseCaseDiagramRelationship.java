package BusinessLayer.Components.UseCaseDiagramComponents;

import BusinessLayer.Components.UMLComponent;
import BusinessLayer.Diagrams.UseCaseDiagram;

import java.awt.*;
import java.awt.event.*;

import static com.sun.java.accessibility.util.AWTEventMonitor.addActionListener;
/*
public class UseCaseDiagramRelationship extends UMLComponent {

    private Point startPoint; // Start of the line
    private Point endPoint;   // End of the line

    private boolean draggingStart = false;
    private boolean draggingEnd = false;

    UMLComponent from;
    UMLComponent to;
    String label;

    public UseCaseDiagramRelationship(UMLComponent from, UMLComponent to, String name) {
        super();
        this.from = from;
        this.to = to;
        this.name = name;
        setPreferredSize(new Dimension(200,200));

        if (name.equalsIgnoreCase("Include")) {
            this.label = "<<include>>";
        } else if (name.equalsIgnoreCase("Extend")) {
            this.label = "<<Extend>>";
        } else {
            this.label = ""; // No label for simple associations
        }

        if (from instanceof Actor && to instanceof UseCase) {
            ((Actor) from).addUseCase((UseCase) to);
        } else if (from instanceof UseCase && to instanceof Actor) {
            ((UseCase) from).addActor((Actor) to);
        }

        this.startPoint = from != null ? from.getPoint() : new Point(50, 50); // Default positions
        this.endPoint = to != null ? to.getPoint() : new Point(150, 50);

    }

    @Override
    protected void paintComponent(Graphics g) {
        /*super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Determine positions
        int x1 = startPoint.x;
        int x2 = endPoint.x;
        int y1 = startPoint.y;
        int y2 = endPoint.y;

        // Draw the line
        if (name.equalsIgnoreCase("Include") ||
                name.equalsIgnoreCase("Extend")) {
            float[] dashPattern = {10, 10};
            g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, dashPattern, 0));

        } else {
            g2d.setStroke(new BasicStroke(2));
        }
        g2d.drawLine(x1, y1, x2, y2);

        // Draw the arrowhead at the end of the line
        drawArrow(g2d, x1, y1, x2, y2);

        // Draw the label (if applicable)
        if (!label.isEmpty()) {
            int labelX = (x1 + x2) / 2;
            int labelY = (y1 + y2) / 2;
            //g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            g2d.setColor(Color.BLACK);
            g2d.drawString(label, labelX, labelY - 5);
        }
        // Optional: Draw small circles around the start and end points for better visibility
        if(isSelected()) {
            g2d.setColor(Color.RED);
            g2d.fillOval(startPoint.x - 5, startPoint.y - 5, 10, 10); // Circle around the start point
            g2d.fillOval(endPoint.x - 5, endPoint.y - 5, 10, 10);     // Circle around the end point
        }

        if(isDraggingStart() || isDraggingEnd()){
            // Calculate the bounding rectangle of the line
            int x = Math.min(x1, x2) - 5; // Add some padding for better visibility
            int y = Math.min(y1, y2) - 5;
            int width = Math.abs(x2 - x1) + 10; // Add padding
            int height = Math.abs(y2 - y1) + 10;

            // Set color to blue
            g2d.setColor(Color.BLUE);

            // Set dashed stroke
            float[] dashPattern = {5, 5}; // Shorter dash for rectangle
            g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, dashPattern, 0));

            // Draw the dashed rectangle
            g2d.drawRect(x, y, width, height);

            revalidate(); // Notify the layout manager
        }
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Calculate start and end points relative to the component bounds
        int x1 = startPoint.x - getX();
        int y1 = startPoint.y - getY();
        int x2 = endPoint.x - getX();
        int y2 = endPoint.y - getY();

        // Set the stroke based on the relationship type
        if (name.equalsIgnoreCase("Include") || name.equalsIgnoreCase("Extend")) {
            float[] dashPattern = {10, 10};
            g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, dashPattern, 0));
        } else {
            g2d.setStroke(new BasicStroke(2));
        }

        // Draw the line
        g2d.drawLine(x1, y1, x2, y2);

        // Draw the arrowhead
        drawArrow(g2d, x1, y1, x2, y2);

        // Draw the label (if applicable)
        if (!label.isEmpty()) {
            int labelX = (x1 + x2) / 2;
            int labelY = (y1 + y2) / 2 - 5;
            g2d.setColor(Color.BLACK);
            g2d.drawString(label, labelX, labelY);
        }

        // Highlight endpoints when selected
        if (isSelected()) {
            g2d.setColor(Color.RED);
            g2d.fillOval(x1 - 5, y1 - 5, 10, 10);
            g2d.fillOval(x2 - 5, y2 - 5, 10, 10);
        }
    }

    private void drawArrow(Graphics2D g2d, int x1, int y1, int x2, int y2) {
        // Calculate the angle of the line
        double angle = Math.atan2(y2 - y1, x2 - x1);

        // Arrowhead dimensions
        int arrowLength = 10;
        int arrowWidth = 5;

        // Points for the arrowhead
        int xArrow1 = (int) (x2 - arrowLength * Math.cos(angle - Math.PI / 6));
        int yArrow1 = (int) (y2 - arrowLength * Math.sin(angle - Math.PI / 6));
        int xArrow2 = (int) (x2 - arrowLength * Math.cos(angle + Math.PI / 6));
        int yArrow2 = (int) (y2 - arrowLength * Math.sin(angle + Math.PI / 6));

        // Draw the arrowhead
        g2d.setStroke(new BasicStroke(1));
        g2d.drawLine(x2, y2, xArrow1, yArrow1);
        g2d.drawLine(x2, y2, xArrow2, yArrow2);
    }

    private boolean isNearStartPoint(MouseEvent e) {
        int threshold = 15; // Distance threshold to detect if near the start point
        return e.getPoint().distance(startPoint) < threshold;
    }

    private boolean isNearEndPoint(MouseEvent e) {
        int threshold = 15; // Distance threshold to detect if near the end point
        return e.getPoint().distance(endPoint) < threshold;
    }

    public void handleUseCaseRelationshipMousePressed(MouseEvent e){
        if (isNearStartPoint(e)) {
            draggingStart = true;
        } else if (isNearEndPoint(e)) {
            draggingEnd = true;
        }
    }

    public void handleUseCaseRelationshipMouseReleased(MouseEvent e){
        draggingStart = false;
        draggingEnd = false;
    }

    public void handleUseCaseRelationshipMouseDragged(MouseEvent e){

        if (draggingStart) {
            startPoint = e.getPoint();
            repaint();
        } else if (draggingEnd) {
            endPoint = e.getPoint();
            repaint();
        }

        int minX = Math.min(startPoint.x, endPoint.x) - 10; // Add some padding
        int minY = Math.min(startPoint.y, endPoint.y) - 10;
        int width = Math.abs(endPoint.x - startPoint.x) + 20; // Add padding
        int height = Math.abs(endPoint.y - startPoint.y) + 20;

        startPoint = new Point(startPoint.x - minX, startPoint.y - minY);
        endPoint = new Point(endPoint.x - minX, endPoint.y - minY);

        setBounds(minX, minY, width, height);
    }

    // Optional: Getters and setters for start and end points
    public Point getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
        repaint();
    }

    public Point getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(Point endPoint) {
        this.endPoint = endPoint;
        repaint();
    }

    public UMLComponent getFrom() {
        return from;
    }

    public void setFrom(UMLComponent from) {
        this.from = from;
    }

    public UMLComponent getTo() {
        return to;
    }

    public void setTo(UMLComponent to) {
        this.to = to;
    }

    @Override
    public void draw(Graphics g) {

    }

    public boolean isDraggingStart() {
        return draggingStart;
    }

    public boolean isDraggingEnd() {
        return draggingEnd;
    }
}*/
public class UseCaseDiagramRelationship extends UMLComponent {

    Point startPoint;
    Point endPoint;

    private UMLComponent from;
    private UMLComponent to;
    private String label;

    public UseCaseDiagramRelationship(UMLComponent from, UMLComponent to, String name) {
        super();
        this.from = from;
        this.to = to;
        this.name = name;
        this.label = "";

        // Attach listeners to update bounds when 'from' or 'to' move
        if (from != null && to != null) {

            if(from instanceof Actor && to instanceof UseCase){
                ((Actor) from).addUseCase((UseCase) to); // this will add actor in use case too
            } else if(from instanceof UseCase && to instanceof Actor){
                ((UseCase) from).addActor((Actor) to);
            } // if its two use cases we don't gaf


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
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        System.out.println(getName() + " paint component called");

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Calculate line endpoints relative to this component's bounds
        if(from !=null && to != null) {
            startPoint = calculateConnectionPoint(from, to);
            endPoint = calculateConnectionPoint(to, from);
        }

        System.out.println("Painting line from " + startPoint + " to " + endPoint);

        if (name.equalsIgnoreCase("Include") ||
                name.equalsIgnoreCase("Extend")) {

            float[] dashPattern = {10, 10};
            g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, dashPattern, 0));

            this.label = name.equalsIgnoreCase("Include") ? "<<include>>" :
                    name.equalsIgnoreCase("Extend") ? "<<Extend>>" : "";
        } else {
            g2d.setStroke(new BasicStroke(2));
        }
        g2d.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);

        // Draw the arrowhead at the end of the line
        drawArrow(g2d, startPoint.x, startPoint.y, endPoint.x, endPoint.y);

        // Draw the label (if applicable)
        if (!label.isEmpty()) {
            int labelX = (startPoint.x + endPoint.x) / 2;
            int labelY = (startPoint.y + endPoint.y) / 2;
            //g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            g2d.setColor(Color.BLACK);
            g2d.drawString(label, labelX, labelY - 5);
        }
        if (isSelected()) {
            g2d.setColor(Color.BLUE);
            g2d.fillOval(startPoint.x - 5, startPoint.y - 5, 10, 10);
            g2d.fillOval(endPoint.x - 5, endPoint.y - 5, 10, 10);
        }
    }

    private void drawArrow(Graphics2D g2d, int x1, int y1, int x2, int y2) {
        // Calculate the angle of the line
        double angle = Math.atan2(y2 - y1, x2 - x1);

        // Arrowhead dimensions
        int arrowLength = 10;
        int arrowWidth = 5;

        // Points for the arrowhead
        int xArrow1 = (int) (x2 - arrowLength * Math.cos(angle - Math.PI / 6));
        int yArrow1 = (int) (y2 - arrowLength * Math.sin(angle - Math.PI / 6));
        int xArrow2 = (int) (x2 - arrowLength * Math.cos(angle + Math.PI / 6));
        int yArrow2 = (int) (y2 - arrowLength * Math.sin(angle + Math.PI / 6));

        // Draw the arrowhead
        g2d.setStroke(new BasicStroke(1));
        g2d.drawLine(x2, y2 , xArrow1, yArrow1);
        g2d.drawLine(x2, y2 , xArrow2, yArrow2);
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
            int width = Math.abs(endPoint.x - startPoint.x) + 20; // Add padding for width
            int height = Math.abs(endPoint.y - startPoint.y) + 20; // Add padding for height

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

    @Override
    public void updateFromTextArea() {

    }

    @Override
    public void draw(Graphics g) {

    }

    public void setManualBounds(Point start, Point end) {
        this.startPoint = start;
        this.endPoint = end;
        updateBounds();
        revalidate();
        repaint();
    }
}