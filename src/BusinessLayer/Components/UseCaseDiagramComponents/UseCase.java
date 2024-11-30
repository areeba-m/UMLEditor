package BusinessLayer.Components.UseCaseDiagramComponents;

import BusinessLayer.Components.UMLComponent;

import java.awt.*;
import java.awt.event.MouseEvent;

public class UseCase extends UMLComponent {
    int width;
    int height;

    public UseCase(String name) {
        super();
        this.name = name;
        setPreferredSize(new Dimension(200,200));

        this.point = new Point(0,0);
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
        if (isSelected()) {
            float[] dashPattern = {5, 5}; // Dashed line pattern
            g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, dashPattern, 0));
            g2.setColor(Color.BLUE);

            // Dashed rectangle slightly larger than the oval
            int rectPadding = 2; // Additional padding around the oval
            g2.drawRect(ovalX - rectPadding, ovalY - rectPadding, ovalWidth + rectPadding * 2, ovalHeight + rectPadding * 2);
        }
    }

    public void handleUseCaseMousePressed(MouseEvent e){

    }

    public void handleUseCaseMouseReleased(MouseEvent e){

    }

    public void handleUseCaseMouseDragged(MouseEvent e){

    }

    @Override
    public void draw(Graphics g) {

    }
}