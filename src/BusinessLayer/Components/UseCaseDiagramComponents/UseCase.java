package BusinessLayer.Components.UseCaseDiagramComponents;

import BusinessLayer.Components.UMLComponent;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.json.JSONObject;

import java.awt.*;
import java.awt.event.MouseEvent;
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
public class UseCase extends UMLComponent {
    private ArrayList<Actor> actors = new ArrayList<>();

    public UseCase(String name) {
        super();
        this.name = name;
        setPreferredSize(new Dimension(200,200));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Measure the size of the name text
        FontMetrics metrics = g2.getFontMetrics(getFont());
        int textWidth = metrics.stringWidth(name);
        int textHeight = metrics.getHeight();

        // Adjust oval size based on text size with padding
        int padding = 20; // Padding around the text inside the oval
        int ovalWidth = textWidth + padding * 2; // Total width of the oval
        int ovalHeight = textHeight + padding;  // Total height of the oval

        // Center the oval in the component
        int ovalX = (getWidth() - ovalWidth) / 2;
        int ovalY = (getHeight() - ovalHeight) / 2;

        // Set the new bounds of the component based on the oval size
        int newWidth = ovalWidth + 10;
        int newHeight = ovalHeight + 10;
        setBounds(getX(), getY(), newWidth, newHeight);

        // Draw the filled oval
        g2.setColor(Color.WHITE);
        g2.fillOval(ovalX, ovalY, ovalWidth, ovalHeight);

        // Draw the oval's border
        g2.setColor(Color.BLACK);
        g2.drawOval(ovalX, ovalY, ovalWidth, ovalHeight);

        // Draw the name inside the oval, centered
        int textX = ovalX + (ovalWidth - textWidth) / 2;
        int textY = ovalY + ((ovalHeight - textHeight) / 2) + metrics.getAscent();
        g2.drawString(name, textX, textY);

        // Draw dashed boundary if selected

        float[] dashPattern = {5, 5}; // Dashed line pattern
        g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, dashPattern, 0));
        g2.setColor(Color.BLUE);

        // Dashed rectangle slightly larger than the oval
        int rectPadding = 2; // Additional padding around the oval
        if (isSelected()) {
            g2.drawRect(ovalX - rectPadding, ovalY - rectPadding, ovalWidth + rectPadding * 2, ovalHeight + rectPadding * 2);
        }
        Point p = new Point(ovalX - rectPadding, ovalY - rectPadding);
        setPoint(p);
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
        json.put("type", "UseCase");
        json.put("name", this.name);
        Rectangle bounds = this.getBounds(); // Assuming getBounds() is implemented
        json.put("bounds", new JSONObject()
                .put("x", bounds.x)
                .put("y", bounds.y)
                .put("width", bounds.width)
                .put("height", bounds.height));

        return json;
    }

    public void addActor(Actor actor) {
        if (!actors.contains(actor)) {
            actors.add(actor);
            actor.addUseCase(this); // Bidirectional relationship
        }
    }

    public void removeActor(Actor actor) {
        if (actors.contains(actor)) {
            actors.remove(actor);
            actor.removeUseCase(this); // Bidirectional relationship
        }
    }

}