package BusinessLayer.Diagrams;

import BusinessLayer.Components.UMLComponent;

import java.awt.*;
import java.util.ArrayList;

public abstract class UMLDiagram {
    String name;
    ArrayList<UMLComponent> components;

    public abstract void addComponent(UMLComponent component);
    public abstract void removeComponent(UMLComponent component);
    public abstract void renderComponents(Graphics g);//draws all components on the canvas
}