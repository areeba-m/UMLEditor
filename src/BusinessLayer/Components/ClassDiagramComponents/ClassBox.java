package BusinessLayer.Components.ClassDiagramComponents;

import BusinessLayer.Components.UMLComponent;
import ui.UMLEditorForm;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class ClassBox extends UMLComponent {
    ArrayList<String> attributes;
    ArrayList<String> methods;
    int height;
    int width;
    //String classType; shifted to UMLComponent

    public ClassBox()
    {
        setPreferredSize(new Dimension(200,200));

        this.point = new Point(0,0);
        this.name = "";
        this.classType = "";
        this.attributes = new ArrayList<>();
        this.methods = new ArrayList<>();

        this.height = 60;
        this.width = 100;

        this.isGridPanel = false;
        this.isDropped = false;
        setSelected(false);//component is not selected initially
        textArea = UMLEditorForm.getTextArea();
    }

    @Override
    public void updateFromTextArea() {
        String[] sections = textArea.getText().split("\n----------------------------------------\n");

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
        addAttribute(attributes);
        addMethods(methods);
        repaint();
    }
//    public ClassBox(String name, String type)
//    {
//        this.point = new Point(0,0);
//        this.name = name;
//        this.classType = type;
//        this.attributes = new ArrayList<>();
//        this.methods = new ArrayList<>();
//        isGridPanel = false;
//
////        addMouseListener(new MouseAdapter() {
////            @Override
////            public void mousePressed(MouseEvent e) {
////            }
////        });
//    }
//
    public void handleClassBoxPressed(MouseEvent e)
    {
        //when the classbox is selected set isSelected to true to identify it in the form
        setSelected(true);
        // Check if the click is inside this ClassBox
            // Pass this ClassBox to UMLEditorForm so that it can update its attributes and methods
            //form.setSelectedClassBox(ClassBox.this);
            // Call updateTextArea method when clicked
//            if(isDropped) {
//                String text = name + "\n";
//                text += "----------------------------------------------------" + "\n";
//                for (int i = 0; i < attributes.size(); i++) {
//                    text += "- ";
//                    text += attributes.get(i);
//                    text += "\n";
//                }
//                text += "----------------------------------------------------" + "\n";
//                for (int i = 0; i < methods.size(); i++) {
//                    text += "- ";
//                    text += methods.get(i);
//                    text += "\n";
//                }
//                text += "----------------------------------------------------" + "\n";
//                updater.updateTextArea(text);
//                return true;
//            }
//            return false;
    }
//    // Check if the mouse click is inside this ClassBox
//    public boolean isClicked(Point point) {
//        Rectangle bounds = getBounds();  // Get the bounding rectangle of the class box
//        return bounds.contains(point);  // Check if the click point is within the box
//    }
//
//    // Perform the desired action when the ClassBox is clicked
//    public void handleClick() {
//        // Call the updateTextArea method to update the UI (in this case, set class name in the text area)
//        //updater.updateTextArea();
//    }.

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
         System.out.println("Height: "+height+", width: "+width);
//         revalidate();
//         repaint();
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
        System.out.println("Height: "+height+", width: "+width);
//        revalidate();
//        repaint();
    }
    public Point getPoint()
    {
        return point;
    }

    public void handleClassBoxMousePressed(MouseEvent e)
    {
        //setSelected(true);
        String text = name;
        text += "\n";
        //starting attributes
        for(int i = 0; i < 40; i++)
        {
            text += "-";
        }
        text += "\n";
        for(int i = 0; i < attributes.size(); i++)
        {
            text += attributes.get(i);
            text += "\n";
        }
        //starting methods
        for(int i = 0; i < 40; i++)
        {
            text += "-";
        }
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
//        setSelected(false);
//        textArea.setText("Add Diagram Notes.");
    }
    public void handleClassBoxMouseDragged(MouseEvent e)
    {
//        this.point = e.getPoint();
//        updatePreferredSize();
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
        int x = (int)point.getX();
        int y = (int)point.getY();

        if (classType.equals("Interface")) {
            int padding = 10; // Padding for text inside the box
            int boxHeight = height + 40; // Initial box height
            int boxWidth = width; // Minimum box width

// Get FontMetrics for measuring string widths
            FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
//
//// Determine the longest string among attributes, methods, and class name
//            String longestString = name; // Start with the class name
//            for (String attribute : attributes) {
//                if (metrics.stringWidth(attribute) > metrics.stringWidth(longestString)) {
//                    longestString = attribute;
//                }
//            }
//            for (String method : methods) {
//                if (metrics.stringWidth(method) > metrics.stringWidth(longestString)) {
//                    longestString = method;
//                }
//            }
//
//// Calculate the width based on the longest string
//            int textWidth2 = metrics.stringWidth(longestString);
//            boxWidth = Math.max(boxWidth, textWidth2 + padding * 2); // Add padding for margins
//
//            // Adjust box height based on attributes and methods (if needed)
//            int attributeSectionHeight = 20 * attributes.size();
//            int methodSectionHeight = 20 * methods.size();
//            boxHeight = 50 + attributeSectionHeight + methodSectionHeight + 40;
//
//
//            setPreferredSize(new Dimension(boxWidth, boxHeight));
//            revalidate();
//
            System.out.println("height: "+ boxHeight+ ", width: "+boxWidth);

            // Set up drawing starting coordinates for the box
            x = (int) point.getX();
            y = (int) point.getY();

            // Draw the class box
            g2d.drawRect(x, y, boxWidth, boxHeight);

            // Get font metrics for centering text
            //FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());

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
            int boxHeight = height + 10; // Initial box height
            int boxWidth = width; // Minimum box width

// Get FontMetrics for measuring string widths
            FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
//
//// Determine the longest string among attributes, methods, and class name
//            String longestString = name; // Start with the class name
//            for (String attribute : attributes) {
//                if (metrics.stringWidth(attribute) > metrics.stringWidth(longestString)) {
//                    longestString = attribute;
//                }
//            }
//            for (String method : methods) {
//                if (metrics.stringWidth(method) > metrics.stringWidth(longestString)) {
//                    longestString = method;
//                }
//            }
//
//// Calculate the width based on the longest string
//            int textWidth2 = metrics.stringWidth(longestString);
//            boxWidth = Math.max(boxWidth, textWidth2 + padding * 2); // Add padding for margins
//
//// Adjust height based on attributes and methods
//            int attributeSectionHeight = 20 * attributes.size();
//            int methodSectionHeight = 20 * methods.size();
//            boxHeight = 50 + attributeSectionHeight + methodSectionHeight + 40;
//
//
//            setPreferredSize(new Dimension(boxWidth, boxHeight));
//            revalidate();
//
            System.out.println("height: "+ boxHeight+ ", width: "+boxWidth);

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
            int boxHeight = height + 10; // Initial box height
            int boxWidth = width; // Minimum box width

// Get FontMetrics for measuring string widths
            FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
//
//// Determine the longest string among attributes, methods, and class name
//            String longestString = name; // Start with the class name
//            for (String attribute : attributes) {
//                if (metrics.stringWidth(attribute) > metrics.stringWidth(longestString)) {
//                    longestString = attribute;
//                }
//            }
//            for (String method : methods) {
//                if (metrics.stringWidth(method) > metrics.stringWidth(longestString)) {
//                    longestString = method;
//                }
//            }
//// Calculate the width based on the longest string
//            int textWidth2 = metrics.stringWidth(longestString);
//            boxWidth = Math.max(boxWidth, textWidth2 + padding * 2); // Add padding for margins
//
//// Adjust height based on attributes and methods
//            int attributeSectionHeight = 20 * attributes.size();
//            int methodSectionHeight = 20 * methods.size();
//            boxHeight = 50 + attributeSectionHeight + methodSectionHeight + 40;
//
//            System.out.println("height: "+ boxHeight+ ", width: "+boxWidth);
//
//            setPreferredSize(new Dimension(boxWidth, boxHeight));
//            revalidate();

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
}