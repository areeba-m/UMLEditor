package BusinessLayer.Diagrams;

import BusinessLayer.Components.UMLComponent;

import java.awt.*;
import java.util.ArrayList;

public class ClassDiagram extends UMLDiagram {


    public ClassDiagram() {

        this.components = new ArrayList<>();
        this.name = "";
        setLayout(null);
        setPreferredSize(new Dimension(2000,2000));
        setBackground(Color.PINK);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (UMLComponent component : components) {
            component.repaint();
        }
    }

    @Override
    public void addComponent(UMLComponent component) {
        System.out.println("Add called for component: " + component);

        components.add(component);
        setupComponentForDiagram(component);
        add(component);
        component.setBounds(50, 50, component.getPreferredSize().width, component.getPreferredSize().height);
        revalidate();
        repaint();
    }

    @Override
    public void removeComponent(UMLComponent component) {
        if (components.contains(component)) { // Remove only if present
            components.remove(component);
            remove(component);
            revalidate();
            repaint();
        }
    }


    @Override
    public void renderComponents(Graphics g) {

    }

}