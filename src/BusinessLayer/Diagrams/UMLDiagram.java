package BusinessLayer.Diagrams;

import BusinessLayer.Components.UMLComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.dnd.DropTargetListener;
import java.util.ArrayList;

public abstract class UMLDiagram extends JPanel implements DropTargetListener {
    String name;
    ArrayList<UMLComponent> components;

    public abstract void addComponent(UMLComponent component);
    public abstract void removeComponent(UMLComponent component);
    public abstract void renderComponents(Graphics g);//draws all components on the canvas
}