package BusinessLayer.Components.UseCaseDiagramComponents;

import BusinessLayer.Components.UMLComponent;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.json.JSONObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
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

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int headRadius = 10;   // Radius of the head
        int bodyHeight = 40;   // Height of the body
        int armLength = 20;    // Length of the arms
        int legLength = 30;    // Length of the legs

        // Add margins around the stick figure
        int margin = 20;

        // Get FontMetrics to calculate text dimensions
        FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
        int textWidth = metrics.stringWidth(name); // Width of the name text
        int textHeight = metrics.getHeight();      // Height of the name text

        // Calculate dimensions of the figure
        int figureWidth = Math.max(2 * armLength + margin * 2, textWidth + margin); // Ensure the width accommodates the name
        int figureHeight = 2 * headRadius + bodyHeight + legLength + textHeight + margin + 5;

        // Set the preferred size based on the calculated dimensions
        setPreferredSize(new Dimension(figureWidth, figureHeight));
        setSize(figureWidth, figureHeight);

        // Center coordinates for the stick figure
        int centerX = figureWidth / 2;
        int headY = margin; // Y-coordinate of the head's top

        // Draw the stick figure
        g2d.setColor(Color.BLACK);

        // 1. Head
        g2d.drawOval(centerX - headRadius, headY, 2 * headRadius, 2 * headRadius);

        // 2. Body
        int bodyStartY = headY + 2 * headRadius;
        g2d.drawLine(centerX, bodyStartY, centerX, bodyStartY + bodyHeight);

        // 3. Arms
        int armY = bodyStartY + 10;
        g2d.drawLine(centerX - armLength, armY, centerX + armLength, armY);

        // 4. Legs
        int legStartY = bodyStartY + bodyHeight;
        g2d.drawLine(centerX, legStartY, centerX - armLength, legStartY + legLength); // Left leg
        g2d.drawLine(centerX, legStartY, centerX + armLength, legStartY + legLength); // Right leg

        // Draw the actor's name below the figure
        int textX = (figureWidth - textWidth) / 2; // Center the text horizontally
        int textY = legStartY + legLength + textHeight; // Position text below legs
        g2d.drawString(name, textX, textY);

        // If selected, draw a dashed rectangle around the figure and name
        if (isSelected()) {
            // Save the current stroke
            Stroke originalStroke = g2d.getStroke();

            // Dashed line pattern
            float[] dashPattern = {5.0f, 5.0f};
            g2d.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f, dashPattern, 0.0f));
            g2d.setColor(Color.BLUE);

            // Draw a dashed boundary rectangle
            int rectWidth = Math.max(2 * armLength + margin, textWidth + margin); // Ensure it encloses the name
            int rectHeight = figureHeight - margin / 2 - 2; // Ensure bottom line is below the name
            g2d.drawRect(
                    (figureWidth - rectWidth) / 2, // X-coordinate to center the rectangle
                    margin / 2,                   // Y-coordinate
                    rectWidth,                    // Width
                    rectHeight                    // Height
            );

            Point p = new Point((figureWidth - rectWidth) / 2, margin / 2);
            setPoint(p);
            // Restore the original stroke
            g2d.setStroke(originalStroke);
        }
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
        json.put("type", "Actor");
        json.put("name", this.name);
        Rectangle bounds = this.getBounds(); // Assuming getBounds() is implemented
        json.put("bounds", new JSONObject()
                .put("x", bounds.x)
                .put("y", bounds.y)
                .put("width", bounds.width)
                .put("height", bounds.height));

        // Return the JSON object
        return json;
    }

    public void addUseCase(UseCase useCase) {
        if (!useCases.contains(useCase)) {
            useCases.add(useCase);
            useCase.addActor(this); // Bidirectional relationship
        }
    }

    public void removeUseCase(UseCase useCase) {
        if (useCases.contains(useCase)) {
            useCases.remove(useCase);
            useCase.removeActor(this); // Bidirectional relationship
        }
    }
}