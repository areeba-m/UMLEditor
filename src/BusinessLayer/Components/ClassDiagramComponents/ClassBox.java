package BusinessLayer.Components.ClassDiagramComponents;

import BusinessLayer.Components.UMLComponent;
import BusinessLayer.Components.UseCaseDiagramComponents.UseCase;
import ui.UMLEditorForm;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class ClassBox extends UMLComponent {
    ArrayList<String> attributes;
    ArrayList<String> methods;
    ArrayList<ClassDiagramRelationship> relationships;

    int height;
    int width;
    //String classType; shifted to UMLComponent

    public ClassBox() {
        //setPreferredSize(new Dimension(200, 200));

        this.point = new Point(0, 0);
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
        //String[] sections = textArea.getText().split("\n----------------------------------------\n");
        String[] sections = textArea.getText().split("\n--\n");

        String[] attributes = new String[0];

        String[] methods = new String[0];
        for(int i = 0; i < sections.length; i++)
        {
            if(i == 0)
            {
                setName(sections[0]);
            }
            else if(i == 1)
            {
                attributes  = sections[1].split("\n");
            }
            else if(i == 2)
            {
                methods = sections[2].split("\n");
            }
        }
        if(this.classType.equalsIgnoreCase("interface")){
            addMethods(attributes);
        } else {
            addAttribute(attributes);
            addMethods(methods);
        }
        repaint();
    }

    public boolean checkAttributePresence(String value)
    {
        for (String attribute : attributes) {
            if (attribute.equals(value)) {
                return true;
            }
        }
        return false;
    }
    public boolean checkMethodsPresence(String value)
    {
        for (String method : methods) {
            if (method.equals(value)) {
                return true;
            }
        }
        return false;
    }

     public void addAttribute(String[] list)
     {
         for (String s : list) {
             if (!checkAttributePresence(s)) {
                 attributes.add(s);
             }
         }
         updatePreferredSize();
         // Update bounds to avoid rendering issues
         setBounds(getX(), getY(), getPreferredSize().width, getPreferredSize().height);
         //System.out.println("Height: "+height+", width: "+width);
         //revalidate();
         //repaint();
     }
     public void addMethods(String[] list)
     {
         for (String s : list) {
             if (!checkMethodsPresence(s)) {
                 methods.add(s);
             }
         }
         updatePreferredSize();
         // Update bounds to avoid rendering issues
         setBounds(getX(), getY(), getPreferredSize().width, getPreferredSize().height);
         //System.out.println("Height: " + height + ", width: " + width);
        // revalidate();
        // repaint();
    }

    public void handleClassBoxMousePressed(MouseEvent e)
    {
        String text = name.concat("\n--\n");
        for(String attribute:attributes){
            text = text.concat(attribute + "\n");
        }
        text = text.concat("--\n");
        for(String method:methods){
            text = text.concat(method + "\n");
        }
        System.out.println("Text: " + text);
        textArea.removeAll();
        textArea.setText(text);

        // Update bounds to avoid rendering issues
        setBounds(getX(), getY(), getPreferredSize().width, getPreferredSize().height);

        // Repaint parent to clear artifacts
        getParent().repaint();
    }
    public void handleClassBoxMouseReleased(MouseEvent e)
    {
        // setSelected(false);
        // textArea.setText("Add Diagram Notes.");
    }
    public void handleClassBoxMouseDragged(MouseEvent e)
    {
        // this.point = e.getPoint();
        // updatePreferredSize();
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
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        //System.out.println("PaintComponent called from ClassBox");
        // Set the rendering hints for better text and graphics quality
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Define padding and box dimensions
        int x,y, boxHeight, boxWidth;

        if (classType.equals("Interface")) {
            int padding = 10; // Padding for text inside the box
            boxHeight = height + 40; // Initial box height
            boxWidth = width; // Minimum box width

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
                g2d.drawString(attribute, x + padding, attributeY);
                attributeY += 20;
            }

            // Draw divider line for methods section
            int dividerY2 = attributeY + 5;
            g2d.drawLine(x, dividerY2, x + boxWidth, dividerY2);

            int methodY = dividerY2 + padding +10;
            for (String method : methods) {
                g2d.drawString(method, x + padding, methodY);
                methodY += 20;
            }
        }

        else if (classType.equals("Abstract"))
        {
            int padding = 10; // Padding for text inside the box
            boxHeight = height + 10; // Initial box height
            boxWidth = width; // Minimum box width

            FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
            //System.out.println("height: "+ boxHeight+ ", width: "+boxWidth);

            // Set up drawing starting coordinates for the box
            x = (int) point.getX();
            y = (int) point.getY();

            // Draw the class box
            g2d.drawRect(x, y, boxWidth, boxHeight);

            // Set font to italic for class name
            Font originalFont = g2d.getFont();
            g2d.setFont(new Font("Segoe UI Emoji", Font.ITALIC, 14));

            // Get font metrics for centering the class name
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
                g2d.drawString(attribute, x + padding, attributeY);
                attributeY += 20;
            }

            // Draw divider line for methods section
            int dividerY2 = attributeY + 5;
            g2d.drawLine(x, dividerY2, x + boxWidth, dividerY2);

            // Draw methods
            int methodY = dividerY2 + padding+10;
            for (String method : methods) {
                g2d.drawString(method, x + padding, methodY);
                methodY += 20;
            }
        }
        else
        {
            int padding = 10; // Padding for text inside the box
            boxHeight = height + 10; // Initial box height
            boxWidth = width; // Minimum box width

            // Get FontMetrics for measuring string widths
            FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());

            x = (int) point.getX();
            y = (int) point.getY();

            // Draw the class box
            g2d.drawRect(x, y, boxWidth, boxHeight);

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
                g2d.drawString(attribute, x + padding, attributeY);
                attributeY += 20;
            }

            // Draw divider line for methods section
            int dividerY2 = attributeY + 5;
            g2d.drawLine(x, dividerY2, x + boxWidth, dividerY2);

            // Draw methods
            int methodY = dividerY2 + padding+10;
            for (String method : methods) {
                g2d.drawString(method, x + padding, methodY);
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
            // if relationship is inheritance & current class is a child,
            // check for abstract methods & update my method list

            //if(relationship.getFrom().equals(this)){
                // get parent's
            //}









            relationships.add(relationship);
        }
    }

    public void removeRelationship(ClassDiagramRelationship relationship) {
        relationships.remove(relationship);
    }

    public ArrayList<ClassDiagramRelationship> getRelationships() {
        return relationships;
    }

    public ArrayList<String> getAttributes() {
        return attributes;
    }

    public ArrayList<String> getMethods() {
        return methods;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getWidth() {
        return width;
    }
}
/*
public class ClassBox extends UMLComponent {
    ArrayList<String> attributes;
    ArrayList<String> methods;
    int height;
    int width;

    public ClassBox() {
        this.point = new Point(0, 0);
        this.name = "";
        this.classType = "";
        this.attributes = new ArrayList<>();
        this.methods = new ArrayList<>();

        this.height = 60;
        this.width = 100;

        this.isGridPanel = false;
        this.isDropped = false;
        setSelected(false); // Component is not selected initially
        textArea = UMLEditorForm.getTextArea();

        // Ensure the component has an opaque background
        setOpaque(true);
        setBackground(Color.WHITE);
    }

    @Override
    public void updateFromTextArea() {
        String[] sections = textArea.getText().split("\n--\n");

        String[] attributes = new String[0];
        String[] methods = new String[0];

        for (int i = 0; i < sections.length; i++) {
            if (i == 0) {
                setName(sections[0]);
            } else if (i == 1) {
                attributes = sections[1].split("\n");
            } else if (i == 2) {
                methods = sections[2].split("\n");
            }
        }

        addAttribute(attributes);
        addMethods(methods);
        updateBounds();
        repaint();
    }

    private void updateBounds() {
        // Update the bounds of the ClassBox based on its calculated preferred size
        updatePreferredSize();
        setBounds(getX(), getY(), getPreferredSize().width, getPreferredSize().height);
        getParent().repaint(); // Ensure the parent container updates
    }

    public void addAttribute(String[] list) {
        for (String s : list) {
            if (!checkAttributePresence(s)) {
                attributes.add(s);
            }
        }
        updateBounds();
    }

    public void addMethods(String[] list) {
        for (String s : list) {
            if (!checkMethodsPresence(s)) {
                methods.add(s);
            }
        }
        updateBounds();
    }

    private boolean checkAttributePresence(String value) {
        for (String attribute : attributes) {
            if (attribute.equals(value)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkMethodsPresence(String value) {
        for (String method : methods) {
            if (method.equals(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Set anti-aliasing for better visuals
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Ensure white opaque background
        g2d.setColor(getBackground());
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Define padding and box dimensions
        int padding = 10;
        int yOffset = padding;

        // Draw the class name or interface/abstract type
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 14));
        FontMetrics metrics = g2d.getFontMetrics();

        String headerText = classType.equals("Interface") ? "<<Interface>> " + name
                : classType.equals("Abstract") ? name + " (Abstract)"
                : name;
        int textWidth = metrics.stringWidth(headerText);

        g2d.drawString(headerText, (getWidth() - textWidth) / 2, yOffset + metrics.getAscent());
        yOffset += metrics.getHeight() + padding;

        // Draw a divider line
        g2d.drawLine(padding, yOffset, getWidth() - padding, yOffset);
        yOffset += padding;

        // Draw attributes
        for (String attribute : attributes) {
            g2d.drawString(attribute, padding, yOffset + metrics.getAscent());
            yOffset += metrics.getHeight();
        }

        // Draw a divider line for methods
        g2d.drawLine(padding, yOffset, getWidth() - padding, yOffset);
        yOffset += padding;

        // Draw methods
        for (String method : methods) {
            g2d.drawString(method, padding, yOffset + metrics.getAscent());
            yOffset += metrics.getHeight();
        }

        // If selected, draw a dashed border
        if (isSelected()) {
            g2d.setColor(Color.BLUE);
            Stroke originalStroke = g2d.getStroke();
            float[] dashPattern = {5, 5};
            g2d.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, dashPattern, 0));
            g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            g2d.setStroke(originalStroke);
        }
    }

    @Override
    public void draw(Graphics g) {
        // Drawing logic for the UML diagram (if necessary)
    }

    public void handleClassBoxMousePressed(MouseEvent e) {
        // Update bounds on mouse press (to ensure resizing works properly)
        updateBounds();
    }

    public void handleClassBoxMouseDragged(MouseEvent e) {
        // Allow dragging while updating bounds and parent repaint
        if (point != null) {
            Point currentLocation = getLocation();
            int deltaX = e.getX() - point.x;
            int deltaY = e.getY() - point.y;

            setLocation(currentLocation.x + deltaX, currentLocation.y + deltaY);
            updateBounds();
        }
    }


    public void updatePreferredSize() {
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
        int boxWidth = maxTextWidth + 20; // Include horizontal padding
        int boxHeight = 40 + (20 * attributes.size()) + (20 * methods.size());

        // Account for additional space if Interface or Abstract
        if (classType.equals("Interface")) {
            boxHeight += 20;
        }

        this.width = boxWidth;
        this.height = boxHeight;

        setPreferredSize(new Dimension(boxWidth, boxHeight));
        revalidate();
    }
}*/
