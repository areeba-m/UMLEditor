package BusinessLayer.Diagrams;

import BusinessLayer.Components.UMLComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public abstract class UMLDiagram extends JPanel {
    String name;
    public static ArrayList<UMLComponent> components;

    public UMLDiagram(){
        setLayout(null);
        setPreferredSize(new Dimension(1000,1000));
        //setBackground(Color.PINK);
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
            }
        });
    }

    public ArrayList<UMLComponent> getComponentArr() {
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
}