package BusinessLayer.Components.ClassDiagramComponents;

import BusinessLayer.Components.UMLComponent;

import java.awt.*;
import java.util.ArrayList;

public class ClassBox extends UMLComponent {
    ArrayList<String> attributes;
    ArrayList<String> methods;
    String className;
    String classType;

    public ClassBox()
    {
        this.point = new Point();
        this.className = "";
        this.classType = "";
        this.attributes = new ArrayList<>();
        this.methods = new ArrayList<>();
    }
    public ClassBox(String name, String type)
    {
        this.point = new Point(0,0);
        this.className = name;
        this.classType = type;
        this.attributes = new ArrayList<>();
        this.methods = new ArrayList<>();
        repaint();
    }
    public Point getPoint()
    {
        return point;
    }
    @Override
    public void draw(Graphics g) {

    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Set the rendering hints for better text and graphics quality
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Define padding and box dimensions
        int x = (int)point.getX();
        int y = (int)point.getY();
        
        if (classType.equals("Interface")) {
            int padding = 10;
            int boxWidth = 100;
            int boxHeight = 60;

            // Adjust box height based on attributes and methods (if needed)
            int attributeSectionHeight = 20 * attributes.size();
            int methodSectionHeight = 20 * methods.size();
            boxHeight += attributeSectionHeight + methodSectionHeight + 40;

            // Set up drawing starting coordinates for the box
            x = (int) point.getX();
            y = (int) point.getY();

            // Draw the class box
            g2d.drawRect(x, y, boxWidth, boxHeight);

            // Get font metrics for centering text
            FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());

            // Draw "<<Interface>>" centered in the upper part of the box
            String classHeader = "<<Interface>>";
            int textWidth = metrics.stringWidth(classHeader);
            int textHeight = metrics.getHeight();

            int xInterface = x + (boxWidth / 2) - (textWidth / 2); // Center horizontally
            int yInterface = y + padding + metrics.getAscent(); // Position just below top padding

            g2d.drawString(classHeader, xInterface, yInterface);

            // Draw className centered just below "<<Interface>>"
            classHeader = className;
            textWidth = metrics.stringWidth(classHeader);

            int xClassName = x + (boxWidth / 2) - (textWidth / 2); // Center horizontally
            int yClassName = yInterface + textHeight + padding; // Position below "<<Interface>>"

            g2d.drawString(classHeader, xClassName, yClassName);

            // Draw divider line for attributes section
            int dividerY1 = y + 60;
            g2d.drawLine(x, dividerY1, x + boxWidth, dividerY1);

            // Leave attributes and methods sections empty
            int attributeY = dividerY1 + padding;
            for (String attribute : attributes) {
                g2d.drawString(attribute, x + padding, attributeY);
                attributeY += 20;
            }

            // Draw divider line for methods section
            int dividerY2 = attributeY + 5;
            g2d.drawLine(x, dividerY2, x + boxWidth, dividerY2);

            int methodY = dividerY2 + padding;
            for (String method : methods) {
                g2d.drawString(method, x + padding, methodY);
                methodY += 20;
            }
        }

        else if (classType.equals("Abstract"))
        {
            int padding = 10;
            int boxWidth = 100;
            int boxHeight = 50;

// Adjust height based on attributes and methods
            int attributeSectionHeight = 20 * attributes.size();
            int methodSectionHeight = 20 * methods.size();
            boxHeight += attributeSectionHeight + methodSectionHeight + 40;

// Set up drawing starting coordinates for the box
            x = (int) point.getX();
            y = (int) point.getY();

// Draw the class box
            g2d.drawRect(x, y, boxWidth, boxHeight);

// Set font to italic for class name
            Font originalFont = g2d.getFont();
            g2d.setFont(new Font("Segoe UI Emoji", Font.ITALIC, 14));

// Get font metrics for centering the class name
            FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
            String classHeader = className;
            int textWidth = metrics.stringWidth(classHeader);
            int textHeight = metrics.getHeight();

// Calculate the position to center the class name horizontally and vertically
            int xCentered = x + (boxWidth / 2) - (textWidth / 2);
            int yCentered = y + (30 / 2) + (textHeight / 4);

// Draw the class name centered in the box
            g2d.drawString(classHeader, xCentered, yCentered);

// Reset font back to original
            g2d.setFont(originalFont);

// Draw divider line for attributes section
            int dividerY1 = y + 30;
            g2d.drawLine(x, dividerY1, x + boxWidth, dividerY1);

// Draw attributes
            int attributeY = dividerY1 + padding;
            for (String attribute : attributes) {
                g2d.drawString(attribute, x + padding, attributeY);
                attributeY += 20;
            }

// Draw divider line for methods section
            int dividerY2 = attributeY + 5;
            g2d.drawLine(x, dividerY2, x + boxWidth, dividerY2);

// Draw methods
            int methodY = dividerY2 + padding;
            for (String method : methods) {
                g2d.drawString(method, x + padding, methodY);
                methodY += 20;
            }
        }
        else
        {
            int padding = 10;
            int boxWidth = 100;
            int boxHeight = 50;

// Adjust height based on attributes and methods
            int attributeSectionHeight = 20 * attributes.size();
            int methodSectionHeight = 20 * methods.size();
            boxHeight += attributeSectionHeight + methodSectionHeight + 40;

// Set up drawing starting coordinates for the box
            x = (int) point.getX();
            y = (int) point.getY();

// Draw the class box
            g2d.drawRect(x, y, boxWidth, boxHeight);

// Get font metrics for centering the class name
            FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
            String classHeader = className;
            int textWidth = metrics.stringWidth(classHeader);
            int textHeight = metrics.getHeight();

// Calculate the position to center the class name horizontally and vertically
            int xCentered = x + (boxWidth / 2) - (textWidth / 2);
            int yCentered = y + (30 / 2) + (textHeight / 4);

// Draw the class name centered in the box
            g2d.drawString(classHeader, xCentered, yCentered);

// Draw divider line for attributes section
            int dividerY1 = y + 30;
            g2d.drawLine(x, dividerY1, x + boxWidth, dividerY1);

// Draw attributes
            int attributeY = dividerY1 + padding;
            for (String attribute : attributes) {
                g2d.drawString(attribute, x + padding, attributeY);
                attributeY += 20;
            }

// Draw divider line for methods section
            int dividerY2 = attributeY + 5;
            g2d.drawLine(x, dividerY2, x + boxWidth, dividerY2);

// Draw methods
            int methodY = dividerY2 + padding;
            for (String method : methods) {
                g2d.drawString(method, x + padding, methodY);
                methodY += 20;
            }
        }

    }

}