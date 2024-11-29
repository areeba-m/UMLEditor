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
    ArrayList<UMLComponent> components;

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


}