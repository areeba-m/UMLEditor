package BusinessLayer.Components.ClassDiagramComponents;

import BusinessLayer.Components.UMLComponent;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.json.JSONObject;
import ui.UMLEditorForm;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;

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

public class ClassBox extends UMLComponent {
    ArrayList<String> attributes;
    ArrayList<String> methods;
    ArrayList<ClassDiagramRelationship> relationships;
    int height;
    int width;
    //String classType; shifted to UMLComponent

    public ClassBox()
    {
        //setPreferredSize(new Dimension(200,200));

        this.point = new Point(0,0);
        this.name = "";
        this.classType = "";
        this.attributes = new ArrayList<>();
        this.methods = new ArrayList<>();
        this.relationships = new ArrayList<>();

        this.height = 60;
        this.width = 100;

        this.isGridPanel = false;
        this.isDropped = false;
        setSelected(false);//component is not selected initially
        textArea = UMLEditorForm.getTextArea();
    }

    @Override
    public void updateFromTextArea() {
        String[] sections = textArea.getText().split("\n--\n");

        // Clear existing attributes and methods
        attributes.clear();
        methods.clear();

        // Ensure the class name is always set (if available)
        if (sections.length > 0 && !sections[0].isBlank()) {
            setName(sections[0].trim());
        }
        // Handle attributes section, if present
        String[] attributes = new String[0];
        if (sections.length > 1 && !sections[1].isBlank()) {
            attributes = sections[1].split("\n");
        }
        if(!classType.equals("Interface")) {
            addAttribute(attributes);
        }
        // Handle methods section, if present
        String[] methods = new String[0];
        if (sections.length > 2 && !sections[2].isBlank()) {
            methods = sections[2].split("\n");
        }
        addMethods(methods);

        // Repaint to reflect changes
        repaint();
    }

    public boolean checkAttributePresence(String value)
    {
        for(int i = 0; i < attributes.size();i++)
        {
            if(attributes.get(i).equals(value))
            {
                return true;
            }
        }
        return false;
    }
    public boolean checkMethodsPresence(String value)
    {
        for(int i = 0; i < methods.size();i++)
        {
            if(methods.get(i).equals(value))
            {
                return true;
            }
        }
        return false;
    }

    public void addMethods(String[] list)
    {
        for(int i = 0; i < list.length; i++)
        {
            if(!checkMethodsPresence(list[i])) {
                methods.add(list[i]);
            }
        }
        updatePreferredSize();
        // Update bounds to avoid rendering issues
        setBounds(getX(), getY(), getPreferredSize().width, getPreferredSize().height);
        //point.setLocation(getX(), getY());
        System.out.println("Height: "+height+", width: "+width);
//        revalidate();
//        repaint();
    }

     public void addAttribute(String[] list)
     {
         for(int i = 0; i < list.length; i++)
         {
             if(!checkAttributePresence(list[i])) {
                 attributes.add(list[i]);
             }
         }
         updatePreferredSize();
         // Update bounds to avoid rendering issues
         setBounds(getX(), getY(), getPreferredSize().width, getPreferredSize().height);
         //point.setLocation(getX(), getY());
         System.out.println("Height: "+height+", width: "+width);
//         revalidate();
//         repaint();
     }
    public void setAttributes(ArrayList<String> attributesList) {
        if (attributesList != null) {
            this.attributes.clear();
            this.attributes.addAll((Collection<? extends String>) attributesList);
        }
    }
    public void setPoint(Point point) {
        if(point == null)
        {
            this.point = new Point(point);
        }
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;

    }
    // Setter for methods
    public void setMethods(ArrayList<String> methodsList) {
        if (methodsList != null) {
            this.methods.clear();
            this.methods.addAll((Collection<? extends String>) methodsList);
        }
    }

    public Point getPoint()
    {
        return point;
    }

    public void handleClassBoxMousePressed(MouseEvent e) {
        String text = name;
        text += "\n";
        //starting attributes

            text += "--";
        text += "\n";
        if(attributes.size() <= 0) {
            text += "\n";
        }
        for(int i = 0; i < attributes.size(); i++)
        {
            text += attributes.get(i);
            text += "\n";
        }
        //starting methods
            text += "--";
        text += "\n";
        for(int i = 0; i < methods.size(); i++)
        {
            text += methods.get(i);
            text += "\n";
        }
        textArea.removeAll();
        textArea.setText(text);

        // Update bounds to avoid rendering issues
        setBounds(getX(), getY(), getPreferredSize().width, getPreferredSize().height);
        // Repaint parent to clear artifacts
        getParent().repaint();
    }

    public void handleClassBoxMouseReleased(MouseEvent e)
    {
    }
    public void handleClassBoxMouseDragged(MouseEvent e)
    {
        if (point != null) {
            Point currentLocation = getLocation();
            int deltaX = e.getX() - point.x;
            int deltaY = e.getY() - point.y;

            // Update location
            setLocation(currentLocation.x + deltaX, currentLocation.y + deltaY);
            // Update bounds to avoid rendering issues
            setBounds(getX(), getY(), getPreferredSize().width, getPreferredSize().height);
            // Repaint parent to clear artifacts
            getParent().repaint();
        }
    }
    @Override
    public void draw(Graphics g) {

    }
    @Override
    public void setMethods(java.util.List<Object> methods) {
        if (methods != null) {
            this.methods.clear();
            // Convert Object list to String list and add to methods
            for (Object method : methods) {
                if (method instanceof String) {
                    this.methods.add((String) method);  // Add the method if it's a string
                }
            }
        }
    }
    @Override
    public void setAttributes(java.util.List<Object> attributes) {
        if (attributes != null) {
            this.attributes.clear();
            // Convert Object list to String list and add to attributes
            for (Object attribute : attributes) {
                if (attribute instanceof String) {
                    this.attributes.add((String) attribute);  // Add the attribute if it's a string
                }
            }
        }
    }

    public ArrayList<String> getAttributes()
    {
        return attributes;
    }
    public ArrayList<String> getMethods()
    {
        return methods;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        //System.out.println("PaintComponent called from ClassBox");
        // Set the rendering hints for better text and graphics quality
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        System.out.println(point.getLocation());
        // Define padding and box dimensions
        int x = (int)point.getX();
        int y = (int)point.getY();

        if (classType.equals("Interface")) {
            int padding = 10; // Padding for text inside the box
            int boxHeight = height + 40; // Initial box height
            int boxWidth = width; // Minimum box width

// Get FontMetrics for measuring string widths
            FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());

            //System.out.println("height: "+ boxHeight+ ", width: "+boxWidth);

            // Set up drawing starting coordinates for the box
            x = (int) point.getX();
            y = (int) point.getY();

            // Draw the class box
            g2d.drawRect(x, y, boxWidth, boxHeight);

            // Draw "<<Interface>>" centered in the upper part of the box
            String classHeader = "<<Interface>>";
            int textWidth = metrics.stringWidth(classHeader);
            int textHeight = metrics.getHeight();

            int xInterface = x + (boxWidth / 2) - (textWidth / 2); // Center horizontally
            int yInterface = y + padding + metrics.getAscent(); // Position just below top padding

            g2d.drawString(classHeader, xInterface, yInterface);

            // Draw className centered just below "<<Interface>>"
            classHeader = name;
            textWidth = metrics.stringWidth(classHeader);

            int xClassName = x + (boxWidth / 2) - (textWidth / 2); // Center horizontally
            int yClassName = yInterface + textHeight + padding; // Position below "<<Interface>>"

            g2d.drawString(classHeader, xClassName, yClassName);

            // Draw divider line for attributes section
            int dividerY1 = y + 60;
            g2d.drawLine(x, dividerY1, x + boxWidth, dividerY1);

            // Leave attributes and methods sections empty
            int attributeY = dividerY1 + padding+10;
            for (String attribute : attributes) {
//                g2d.drawString(attribute, x + padding, attributeY);
//                attributeY += 20;
                // Move to the next line
                attributeY += 20;
            }

            // Draw divider line for methods section
            int dividerY2 = attributeY + 5;
            g2d.drawLine(x, dividerY2, x + boxWidth, dividerY2);

            int methodY = dividerY2 + padding +10;
            for (String method : methods) {
                // Check if the method starts and ends with '_'
                if (method.startsWith("_") && method.endsWith("_")) {
                    // Remove the underscores for drawing the string
                    String displayText = method.substring(1, method.length() - 1);
                    g2d.drawString(displayText, x + padding, methodY);

                    // Measure the width of the string to draw the underline
                    int stringWidth = g2d.getFontMetrics().stringWidth(displayText);
                    int underlineY = methodY + 2; // Slightly below the text baseline
                    g2d.drawLine(x + padding, underlineY, x + padding + stringWidth, underlineY);
                }
                // Default case: draw the string as-is
                else {
                    g2d.drawString(method, x + padding, methodY);
                }
                // Move to the next line
                methodY += 20;
            }
        }

        else if (classType.equals("Abstract"))
        {
            int padding = 10; // Padding for text inside the box
            int boxHeight = height + 10; // Initial box height
            int boxWidth = width; // Minimum box width

// Get FontMetrics for measuring string widths
            FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());

           // System.out.println("height: "+ boxHeight+ ", width: "+boxWidth);

// Set up drawing starting coordinates for the box
            x = (int) point.getX();
            y = (int) point.getY();

// Draw the class box
            g2d.drawRect(x, y, boxWidth, boxHeight);

// Set font to italic for class name
            Font originalFont = g2d.getFont();
            g2d.setFont(new Font("Segoe UI Emoji", Font.ITALIC, 14));

// Get font metrics for centering the class name
            //FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
            String classHeader = name;
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
            int attributeY = dividerY1 + padding+10;
            for (String attribute : attributes) {
//                g2d.drawString(attribute, x + padding, attributeY);
//                attributeY += 20;
                if (attribute.startsWith("_") && attribute.endsWith("_")) {//underlining static attribute
                    // Remove the underscores for drawing the string
                    String displayText = attribute.substring(1, attribute.length() - 1);
                    g2d.drawString(displayText, x + padding, attributeY);

                    // Measure the width of the string to draw the underline
                    int stringWidth = g2d.getFontMetrics().stringWidth(displayText);
                    int underlineY = attributeY + 2; // Slightly below the text baseline
                    g2d.drawLine(x + padding, underlineY, x + padding + stringWidth, underlineY);
                } else {
                    // Draw the string as-is
                    g2d.drawString(attribute, x + padding, attributeY);
                }

                // Move to the next line
                attributeY += 20;
            }

// Draw divider line for methods section
            int dividerY2 = attributeY + 5;
            g2d.drawLine(x, dividerY2, x + boxWidth, dividerY2);

// Draw methods
            int methodY = dividerY2 + padding+10;
            for (String method : methods) {
                // Check if the method starts and ends with '/'
                if (method.startsWith("/") && method.endsWith("/")) {
                    // Remove the slashes for drawing the string
                    String displayText = method.substring(1, method.length() - 1);

                    // Set the font to italic
                    Font originalFont2 = g2d.getFont();
                    Font italicFont = new Font("Serif", Font.ITALIC, originalFont2.getSize()); // Use a specific font that supports italic
                    g2d.setFont(italicFont);

                    // Draw the italicized text
                    g2d.drawString(displayText, x + padding, methodY);

                    // Restore the original font
                    g2d.setFont(originalFont2);
                }
                // Check if the method starts and ends with '_'
                else if (method.startsWith("_") && method.endsWith("_")) {
                    // Remove the underscores for drawing the string
                    String displayText = method.substring(1, method.length() - 1);
                    g2d.drawString(displayText, x + padding, methodY);

                    // Measure the width of the string to draw the underline
                    int stringWidth = g2d.getFontMetrics().stringWidth(displayText);
                    int underlineY = methodY + 2; // Slightly below the text baseline
                    g2d.drawLine(x + padding, underlineY, x + padding + stringWidth, underlineY);
                }
                // Default case: draw the string as-is
                else {
                    g2d.drawString(method, x + padding, methodY);
                }
                // Move to the next line
                methodY += 20;
            }
        }
        else
        {
            int padding = 10; // Padding for text inside the box
            int boxHeight = height + 10; // Initial box height
            int boxWidth = width; // Minimum box width

// Get FontMetrics for measuring string widths
            FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
// Set up drawing starting coordinates for the box
            x = (int) point.getX();
            y = (int) point.getY();

// Draw the class box
            g2d.drawRect(x, y, boxWidth, boxHeight);

// Get font metrics for centering the class name
            //FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
            String classHeader = name;
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
            int attributeY = dividerY1 + padding+10;
            for (String attribute : attributes) {
                // Check if the attribute starts and ends with '_'
                if (attribute.startsWith("_") && attribute.endsWith("_")) {
                    // Remove the underscores for drawing the string
                    String displayText = attribute.substring(1, attribute.length() - 1);
                    g2d.drawString(displayText, x + padding, attributeY);

                    // Measure the width of the string to draw the underline
                    int stringWidth = g2d.getFontMetrics().stringWidth(displayText);
                    int underlineY = attributeY + 2; // Slightly below the text baseline
                    g2d.drawLine(x + padding, underlineY, x + padding + stringWidth, underlineY);
                } else {
                    // Draw the string as-is
                    g2d.drawString(attribute, x + padding, attributeY);
                }

                // Move to the next line
                attributeY += 20;
            }

// Draw divider line for methods section
            int dividerY2 = attributeY + 5;
            g2d.drawLine(x, dividerY2, x + boxWidth, dividerY2);

// Draw methods
            int methodY = dividerY2 + padding+10;
            for (String method : methods) {
                // Check if the method starts and ends with '/'
                if (method.startsWith("/") && method.endsWith("/")) {
                    // Remove the slashes for drawing the string
                    String displayText = method.substring(1, method.length() - 1);

                    // Set the font to italic
                    Font originalFont = g2d.getFont();
                    Font italicFont = new Font("Serif", Font.ITALIC, originalFont.getSize()); // Use a specific font that supports italic
                    g2d.setFont(italicFont);

                    // Draw the italicized text
                    g2d.drawString(displayText, x + padding, methodY);

                    // Restore the original font
                    g2d.setFont(originalFont);
                }
                // Check if the method starts and ends with '_'
                else if (method.startsWith("_") && method.endsWith("_")) {
                    // Remove the underscores for drawing the string
                    String displayText = method.substring(1, method.length() - 1);
                    g2d.drawString(displayText, x + padding, methodY);

                    // Measure the width of the string to draw the underline
                    int stringWidth = g2d.getFontMetrics().stringWidth(displayText);
                    int underlineY = methodY + 2; // Slightly below the text baseline
                    g2d.drawLine(x + padding, underlineY, x + padding + stringWidth, underlineY);
                }
                // Default case: draw the string as-is
                else {
                    g2d.drawString(method, x + padding, methodY);
                }
                // Move to the next line
                methodY += 20;
            }
        }
        if (isSelected()) {
            // Save the original stroke
            Stroke originalStroke = g2d.getStroke();

            // Create a dashed stroke
            float[] dashPattern = {5.0f, 5.0f}; // Dash length, gap length
            g2d.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dashPattern, 0.0f));

            // Draw the dashed rectangle
            g2d.setColor(Color.BLUE); // Set the color of the dashed rectangle
            g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

            // Restore the original stroke
            g2d.setStroke(originalStroke);
        }
    }
    public void updatePreferredSize() {
        if(getFont() == null)
        {
            Font font = new Font("Segoe UI Emoji", Font.PLAIN, 14);
            setFont(font);
        }
        FontMetrics metrics = getFontMetrics(getFont());
        int maxTextWidth = metrics.stringWidth(name); // Start with class name width

        // Find the longest attribute or method string
        for (String attribute : attributes) {
            maxTextWidth = Math.max(maxTextWidth, metrics.stringWidth(attribute));
        }
        for (String method : methods) {
            maxTextWidth = Math.max(maxTextWidth, metrics.stringWidth(method));
        }

        // Add padding to the width
        int boxWidth = maxTextWidth + 50;
        int boxHeight = 20 + (20 * attributes.size()) + (20 * methods.size()) + 40;

        height = boxHeight;
        width = boxWidth;
        if(classType.equals("Interface"))
        {
            setPreferredSize(new Dimension(boxWidth, boxHeight+40));

        }
        else {
            // Update preferred size
            setPreferredSize(new Dimension(boxWidth, boxHeight+20));
        }
        revalidate(); // Notify the layout manager to adjust
        repaint();    // Request a repaint for the updated size
    }
    public void addRelationship(ClassDiagramRelationship relationship) {
        if (!relationships.contains(relationship)) {
            relationships.add(relationship);
        }
    }

    public void removeRelationship(ClassDiagramRelationship relationship) {
        relationships.remove(relationship);
    }

    public ArrayList<ClassDiagramRelationship> getRelationships() {
        return relationships;
    }

    //ClassBox
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();

        // Serialize inherited attributes
        json.put("type", "ClassBox");
        json.put("name", this.name);
        json.put("classType", this.classType);

        // Serialize the specific attributes of ClassBox
        json.put("attributes", this.attributes);
        json.put("methods", this.methods);
        json.put("height", this.height);
        json.put("width", this.width);

        // Serialize the point (location)
        json.put("point", new JSONObject()
                .put("x", this.point.x)
                .put("y", this.point.y));

        Rectangle bounds = this.getBounds(); // Assuming getBounds() is implemented
        json.put("bounds", new JSONObject()
                .put("x", bounds.x)
                .put("y", bounds.y)
                .put("width", bounds.width)
                .put("height", bounds.height));

        json.put("location", new JSONObject()
                .put("x", this.getLocation().x)
                .put("y", this.getLocation().y));
        // Return the JSON object
        return json;
    }
}