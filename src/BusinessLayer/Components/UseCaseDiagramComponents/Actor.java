package BusinessLayer.Components.UseCaseDiagramComponents;

import BusinessLayer.Components.UMLComponent;

import java.awt.*;
import java.util.ArrayList;

public class Actor extends UMLComponent {
    ArrayList<UseCase> useCases; //use cases connected to this actor

    public Actor(String name){
        super();
        this.name = name;
        this.useCases = new ArrayList<>();

        setPreferredSize(new Dimension(50, 100));

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Enable antialiasing for smoother graphics
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Calculate dimensions
        int centerX = getWidth() / 2;
        int headRadius = 10; // Head is a small circle
        int bodyHeight = 40; // Height of the body
        int armLength = 20;  // Length of the arms
        int legLength = 30;  // Length of the legs

        // Stick figure components
        // 1. Head (circle)
        g2d.setColor(Color.BLACK);
        g2d.drawOval(centerX - headRadius, 10, 2 * headRadius, 2 * headRadius);

        // 2. Body (vertical line)
        int bodyStartY = 10 + 2 * headRadius;
        g2d.drawLine(centerX, bodyStartY, centerX, bodyStartY + bodyHeight);

        // 3. Arms (two horizontal lines)
        g2d.drawLine(centerX - armLength, bodyStartY + 10, centerX + armLength, bodyStartY + 10);

        // 4. Legs (two diagonal lines)
        int legStartY = bodyStartY + bodyHeight;
        g2d.drawLine(centerX, legStartY, centerX - armLength, legStartY + legLength); // Left leg
        g2d.drawLine(centerX, legStartY, centerX + armLength, legStartY + legLength); // Right leg

        // Optional: Add "Actor" text below the figure
        FontMetrics metrics = g.getFontMetrics(g.getFont());
        int textX = (getWidth() - metrics.stringWidth(name)) / 2;
        int textY = legStartY + legLength + 15; // Position text below legs
        g2d.drawString(this.name, textX, textY);

        if(isSelected()){
            // Save the current stroke
            Stroke originalStroke = g2d.getStroke();

            // Create a dashed stroke
            float[] dashPattern = {5.0f, 5.0f}; // Dash and gap lengths
            g2d.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f, dashPattern, 0.0f));

            // Set a light color
            g2d.setColor(new Color(173, 216, 230, 150)); // Light blue with some transparency

            // Draw the dashed boundary
            int margin = 10; // Space around the actor
            g2d.drawRect(
                    centerX - armLength - margin, // Left boundary
                    20 - margin,                 // Top boundary
                    armLength * 2 + margin * 2,  // Width
                    legStartY + legLength + margin - 10 // Height
            );

            // Restore the original stroke
            g2d.setStroke(originalStroke);
        }
    }

    @Override
    public void draw(Graphics g) {

    }
}