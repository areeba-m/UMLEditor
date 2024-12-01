package BusinessLayer.Diagrams;

import BusinessLayer.Components.UMLComponent;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

@JsonIgnoreProperties({"accessibleContext", "graphicsConfiguration", "rootPane", "layeredPane", "contentPane", "transferHandler", "inputMap", "actionMap", "clientProperty", "focusTraversalPolicyProvider", "focusCycleRoot"}) // Ignore JPanel's internal properties
public abstract class UMLDiagram extends JPanel{
    String name;
    static ArrayList<UMLComponent> components;

    protected UMLComponent selectedComponent; // Track the currently selected component
    protected Point offset; // Offset between the mouse and the top-left corner of the component

    public abstract void addComponent(UMLComponent component);
    public abstract void removeComponent(UMLComponent component);
    public abstract void renderComponents(Graphics g);//draws all components on the canvas


    public UMLDiagram()
    {

    }
    public static ArrayList<UMLComponent> getComponentList()
    {
        return components;
    }

    public abstract ArrayList<UMLComponent> getListOfComponents();
    public void setupComponentForDiagram(UMLComponent component) {
        component.addMouseMotionListener(new MouseAdapter() {
            Point prevPoint;

            @Override
            public void mousePressed(MouseEvent e) {
                prevPoint = e.getPoint();
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
        });
    }

    public UMLComponent getComponentAt(int i)
    {
        return components.get(i);
    }

    //public abstract int getComponentsCount();

    public void saveToFile(String fileName) {
    }

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

    public UMLDiagram loadFromFile(String fileName) {
        return null;
    }

    public abstract int getComponentsCount();
}