package BusinessLayer.Diagrams;

import BusinessLayer.Components.UMLComponent;
import BusinessLayer.Components.UseCaseDiagramComponents.Actor;
import BusinessLayer.Components.UseCaseDiagramComponents.UseCase;
import BusinessLayer.Components.UseCaseDiagramComponents.UseCaseComponent;
import BusinessLayer.Components.UseCaseDiagramComponents.UseCaseDiagramRelationship;
import ui.ConnectionDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class UseCaseDiagram extends UMLDiagram {

    public UseCaseDiagram(){
        super();
        components = new ArrayList<>();

    }

    @Override
    public void addComponent(UMLComponent component) {

        if(components.contains(component)){
            System.out.println("COMPONENT EXISTS: called Add component for " + component);
            return;
        }

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
        components.remove(component);
        remove(component);
        revalidate();
        repaint();
    }

    @Override
    public void renderComponents(Graphics g) {

    }

    @Override
    protected void createConnection(UMLComponent comp1, UMLComponent comp2) {

        JFrame frame = new JFrame("Diagram");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(300, 300);
        frame.setLocationRelativeTo(null);

        ConnectionDialog dialog = new ConnectionDialog(frame, "UseCase");

        if(dialog.getOptionSelected().equalsIgnoreCase("Association")) {
            UseCaseDiagramRelationship relationship = new UseCaseDiagramRelationship(comp1, comp2, "Association");
            addComponent(relationship);
        } else if(dialog.getOptionSelected().equalsIgnoreCase("Include")) {
            if(comp1 instanceof Actor || comp2 instanceof Actor){
                //throw exception;
            }

            UseCaseDiagramRelationship relationship = new UseCaseDiagramRelationship(comp1, comp2, "Include");
            addComponent(relationship);
        } else if(dialog.getOptionSelected().equalsIgnoreCase("Extend")) {
            if(comp1 instanceof Actor || comp2 instanceof Actor){
                //throw exception;
            }

            UseCaseDiagramRelationship relationship = new UseCaseDiagramRelationship(comp1, comp2, "Extend");
            addComponent(relationship);
        }

        revalidate();
        repaint();
    }

}

