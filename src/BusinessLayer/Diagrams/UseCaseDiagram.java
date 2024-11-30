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

import static ui.ConnectionDialog.createRoundedDialog;

public class UseCaseDiagram extends UMLDiagram {

    private UMLComponent selectedComponent1 = null;
    private UMLComponent selectedComponent2 = null;

    public UseCaseDiagram(){
        super();
        components = new ArrayList<>();
        //setLayout(null);
        //setPreferredSize(new Dimension(1000,1000));
        //setBackground(Color.PINK);

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

        // Add click listener to detect connections
        component.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleComponentClick(component);
            }
        });

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

    private void handleComponentClick(UMLComponent component) {
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

    private void createConnection(UMLComponent comp1, UMLComponent comp2) {

        createRoundedDialog();

        UseCaseDiagramRelationship relationship = new UseCaseDiagramRelationship(comp1, comp2, "Include");

        addComponent(relationship);

        revalidate();
        repaint();
    }

}

