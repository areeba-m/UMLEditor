package BusinessLayer.Components.UseCaseDiagramComponents;

import BusinessLayer.Components.UMLComponent;

import java.awt.*;

public class UseCaseDiagramRelationship extends UseCaseComponent {
    UMLComponent from;
    UMLComponent to;
    String type;
    String label;

    public UseCaseDiagramRelationship(UMLComponent from, UMLComponent to, String type) {
        super();
        this.from = from;
        this.to = to;
        this.type = type;

        if (type.equalsIgnoreCase("Include")) {
            this.label = "<<include>>";
        } else if (type.equalsIgnoreCase("Exclude")) {
            this.label = "<<exclude>>";
        } else {
            this.label = ""; // No label for simple associations
        }
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Enable anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Determine positions
        int x1, y1, x2, y2;
        if (from == null || to == null) {
            // Use placeholders coordinates
            x1 = 50;
            y1 = 50;
            x2 = 150;
            y2 = 150;
        } else {
            // Get actual component positions
            x1 = from.getX() + from.getWidth() / 2;
            y1 = from.getY() + from.getHeight() / 2;
            x2 = to.getX() + to.getWidth() / 2;
            y2 = to.getY() + to.getHeight() / 2;
        }

        // Draw the line
        if (type.equalsIgnoreCase("Association")) {
            // Solid line for associations
            g2d.setStroke(new BasicStroke(2));
        } else {
            // Dashed line for include/exclude
            float[] dashPattern = {10, 10}; // Dash length
            g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, dashPattern, 0));
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

}