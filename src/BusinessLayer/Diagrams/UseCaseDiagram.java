package BusinessLayer.Diagrams;

import BusinessLayer.Components.UMLComponent;
import BusinessLayer.Components.UseCaseDiagramComponents.Actor;
import BusinessLayer.Components.UseCaseDiagramComponents.UseCase;
import BusinessLayer.Components.UseCaseDiagramComponents.UseCaseComponent;
import BusinessLayer.Components.UseCaseDiagramComponents.UseCaseDiagramRelationship;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class UseCaseDiagram extends UMLDiagram {

    public UseCaseDiagram(){
        components = new ArrayList<>();
        setLayout(null);
        setPreferredSize(new Dimension(1000,1000));
        setBackground(Color.PINK);

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

    }

    @Override
    public void renderComponents(Graphics g) {

    }


}

