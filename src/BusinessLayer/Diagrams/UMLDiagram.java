package BusinessLayer.Diagrams;

import BusinessLayer.Components.ClassDiagramComponents.ClassDiagramRelationship;
import BusinessLayer.Components.UMLComponent;
import ui.PopupMenu;
import javax.swing.*;
import java.awt.*;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
//import static ui.UMLEditorForm.isConnectMode;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents an abstract UML diagram.
 * Provides a framework for managing UML components, rendering them,
 * and handling user interactions such as drag-and-drop and popup menus.
 */
@JsonIgnoreProperties({"accessibleContext", "graphicsConfiguration", "rootPane", "layeredPane", "contentPane", "transferHandler", "inputMap", "actionMap", "clientProperty", "focusTraversalPolicyProvider", "focusCycleRoot"}) // Ignore JPanel's internal properties
public abstract class UMLDiagram extends JPanel{
    String name;

    private UMLComponent selectedComponent1 = null;
    private UMLComponent selectedComponent2 = null;
    private PopupMenu popupMenu;

    public static ArrayList<UMLComponent> components;

    protected UMLComponent selectedComponent; // Track the currently selected component
    protected Point offset; // Offset between the mouse and the top-left corner of the component

    public abstract void addComponent(UMLComponent component);
    public abstract void removeComponent(UMLComponent component);
    public abstract void renderComponents(Graphics g);//draws all components on the canvas

    /**
     * Constructs a UMLDiagram with default settings, including layout and popup menu.
     */
    public UMLDiagram(){
        setLayout(null);
        setPreferredSize(new Dimension(1000,1000));

        popupMenu = new PopupMenu(this);
    }
    /**
     * Gets the static list of UML components.
     *
     * @return List of UML components.
     */
    public static ArrayList<UMLComponent> getComponentList()
    {
        return components;
    }

    public abstract ArrayList<UMLComponent> getListOfComponents();

    /**
     * Sets up drag-and-drop and mouse interactions for the given UML component.
     *
     * @param component The UML component to be configured.
     */
    public void setupComponentForDiagram(UMLComponent component) {
        component.addMouseMotionListener(new MouseAdapter() {
            Point prevPoint;

            @Override
            public void mousePressed(MouseEvent e) {
                prevPoint = e.getPoint();
                if (!popupMenu.isVisible()) {
                    return;
                }
                popupMenu.setVisible(false);
                System.out.print("Mouse pressed in UML Diagram:" + prevPoint);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (prevPoint != null) {
                    Point currentLocation = component.getLocation();
                    int deltaX = e.getX() - prevPoint.x;
                    int deltaY = e.getY() - prevPoint.y;
                    component.setLocation(currentLocation.x + deltaX, currentLocation.y + deltaY);
                    component.getParent().repaint();
                }
            }
        });

        component.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                component.getParent().repaint();
                //System.out.println(component.getParent());
            }
            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    if (component.isSelected()) {
                        int width = (component.getBounds().width)/2;
                        int height = component.getBounds().height;
                        popupMenu.show(UMLDiagram.this, component.getX() + width, component.getY()+height);
                    }
                }

                /*if(isConnectMode)
                    handleComponentClick(component);*/
            }
        });
    }

    public UMLComponent getComponentAt(int i)
    {
        return components.get(i);
    }
    /**
     * Saves the UML diagram to a file.
     *
     * @param fileName The file name to save the diagram.
     */
    public void saveToFile(String fileName) {
    }

    /**
     * Sets the list of UML components in the diagram.
     *
     * @param componentList List of UML components to be set.
     */
    public void setComponents(ArrayList<UMLComponent> componentList) {
        if (components != null) {
            this.components = new ArrayList<>(componentList);  // Copy the input list to the internal list
            revalidate();  // Revalidate the layout
            repaint();     // Repaint the diagram after updating the components
            System.out.println("Components have been set successfully.");
        } else {
            System.out.println("Error: Provided list of components is null.");
        }
    }

    /**
     * Loads a UML diagram from a file.
     *
     * @param fileName The file name to load the diagram from.
     * @return The loaded UML diagram.
     */
    public UMLDiagram loadFromFile(String fileName) {
        return null;
    }

    public abstract int getComponentsCount();
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        Stroke originalStroke = g2d.getStroke();
        drawGrid(g2d);
        g2d.setStroke(originalStroke);

        // Call renderComponents to draw the components after the grid
        //renderComponents(g);
    }

    // Method to draw a light-colored dotted grid
    private void drawGrid(Graphics2D g2d) {

        // Set the grid color (light gray)
        g2d.setColor(new Color(200, 200, 200));

        // Set the dotted pattern (stroke style)
        float[] dashPattern = {2f, 2f}; // 2px on, 2px off
        g2d.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, dashPattern, 0f));

        // Draw the grid
        int gridSize = 20; // Set the grid spacing

        // Vertical lines
        for (int x = 0; x < getWidth(); x += gridSize) {
            g2d.drawLine(x, 0, x, getHeight());
        }

        // Horizontal lines
        for (int y = 0; y < getHeight(); y += gridSize) {
            g2d.drawLine(0, y, getWidth(), y);
        }
    }

    /*protected void handleComponentClick(UMLComponent component) {
        if (selectedComponent1 == null) {
            selectedComponent1 = component; // First component selected
            System.out.println("Selected first component: " + selectedComponent1.getName());

        } else if (selectedComponent2 == null) {
            selectedComponent2 = component; // Second component selected
            System.out.println("Selected second component: " + selectedComponent2.getName());

            // Create a connection
            createConnection(selectedComponent1, selectedComponent2);

            // Reset selection
            selectedComponent1 = null;
            selectedComponent2 = null;
        }
    }*/

    public abstract int createConnection(UMLComponent comp1, UMLComponent comp2, String type);

    public UMLComponent getSelectedComponent() {
        for (UMLComponent component : components) {
            if (component.isSelected()) {
                return component;
            }
        }
        return null;
    }

    public abstract void addComponents(UMLComponent component);
}