package BusinessLayer.Diagrams;

import BusinessLayer.Components.UMLComponent;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class UMLDiagram extends JPanel{
    String name;

    private UMLComponent selectedComponent1 = null;
    private UMLComponent selectedComponent2 = null;

   public static ArrayList<UMLComponent> components;

    public UMLDiagram(){
        setLayout(null);
        setPreferredSize(new Dimension(1000,1000));

    }

    public abstract void addComponent(UMLComponent component);
    public abstract void removeComponent(UMLComponent component);
    public abstract void renderComponents(Graphics g);//draws all components on the canvas

    public void setupComponentForDiagram(UMLComponent component) {
        component.addMouseMotionListener(new MouseAdapter() {
            Point prevPoint;

            @Override
            public void mousePressed(MouseEvent e) {
                prevPoint = e.getPoint();
                for (UMLComponent component : components) {
                    component.setSelected(false);
                }

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
    public int getComponentsCount()
    {
        return components.size();
    }

    public static ArrayList<UMLComponent> getComponentArr() {
        return components;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Call the parent method to ensure proper panel rendering

        drawGrid(g); // Custom method to draw the dotted grid

        // Call renderComponents to draw the components after the grid
        //renderComponents(g);
    }

    // Method to draw a light-colored dotted grid
    private void drawGrid(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

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

    protected void handleComponentClick(UMLComponent component) {
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
    }
    protected abstract void createConnection(UMLComponent comp1, UMLComponent comp2);

}