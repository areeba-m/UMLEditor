package BusinessLayer.Components.UseCaseDiagramComponents;

import BusinessLayer.Components.ClassDiagramComponents.ClassBox;
import BusinessLayer.Components.UMLComponent;
import BusinessLayer.Diagrams.UseCaseDiagram;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.json.JSONObject;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

import static com.sun.java.accessibility.util.AWTEventMonitor.addActionListener;
@JsonIgnoreProperties({
        "accessibleContext",
        "graphicsConfiguration",
        "rootPane",
        "layeredPane",
        "contentPane",
        "transferHandler",
        "inputMap",
        "actionMap",
        "clientProperty",
        "focusTraversalPolicyProvider",
        "focusCycleRoot",
        "UI", // Another internal property that could be ignored
        "componentOrientation", // Component orientation
        "focusTraversalPolicy", // Focus policy
        "focusOwner" // Current focus owner
})
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
            revalidate();
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

    public void updateBounds() {
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

    @Override
    public void setMethods(List<Object> methods) {

    }

    @Override
    public void setAttributes(List<Object> attributes) {

    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();

        // Serialize inherited attributes
        json.put("type", "UseCaseDiagramRelationship");
        json.put("name", this.name);
        json.put("label", this.label);
        // Serialize the point (location)
        json.put("startPoint", new JSONObject()
                .put("x", this.startPoint.x)
                .put("y", this.startPoint.y));
        json.put("endPoint", new JSONObject()
                .put("x", this.endPoint.x)
                .put("y", this.endPoint.y));

        // Serialize 'from' and 'to' components
        if (from != null) {
            json.put("from", from.toJSON());
        }
        if (to != null) {
            json.put("to", to.toJSON());
        }
        Rectangle bounds = this.getBounds(); // Assuming getBounds() is implemented
        json.put("bounds", new JSONObject()
                .put("x", bounds.x)
                .put("y", bounds.y)
                .put("width", bounds.width)
                .put("height", bounds.height));
        // Return the JSON object
        return json;
    }

    public void setManualBounds(Point start, Point end) {
        this.startPoint = start;
        this.endPoint = end;
        updateBounds();
        revalidate();
        repaint();
    }

    public void setLabel(String relationshipLabel) {
        this.label = relationshipLabel;
    }

    public String getLabel() {
        return label;
    }

    public void setEndPoint(Point point) {
        if(endPoint != null)
        {
            endPoint.setLocation(point.getLocation());
        }
        else {
            endPoint = new Point(point);
        }
        updateBounds();
    }
    public void setStartPoint(Point point) {
        if(this.startPoint != null)
        {
            this.startPoint.setLocation(startPoint.getLocation());
        }
        else {
            this.startPoint = new Point(point);
        }
        updateBounds();
    }
    public Point getEndPoint()
    {
        return endPoint;
    }
    public Point getStartPoint()
    {
        return startPoint;
    }
    public void setFrom(UMLComponent from) {
        this.from = from;
        this.from.setBounds(from.getPoint().x, from.getPoint().y,from.getPreferredSize().width, from.getPreferredSize().height);
    }

    public void setTo(UMLComponent to) {
        this.to = to;
        this.to.setBounds(to.getPoint().x, to.getPoint().y, to.getPreferredSize().width, to.getPreferredSize().height);
    }
//    public void setFrom(UseCase from) {
//        this.from = from;
//        this.from.setBounds(from.getPoint().x, from.getPoint().y,from.getPreferredSize().width, from.getPreferredSize().height);
//    }
//
//    public void setTo(UseCase to) {
//        this.to = to;
//        this.to.setBounds(to.getPoint().x, to.getPoint().y, to.getPreferredSize().width, to.getPreferredSize().height);
//    }
    public UMLComponent getFrom() {
        return from;
    }
    public UMLComponent getTo() {
        return to;
    }

}