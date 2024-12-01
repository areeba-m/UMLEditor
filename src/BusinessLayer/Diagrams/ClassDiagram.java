package BusinessLayer.Diagrams;

import BusinessLayer.Components.ClassDiagramComponents.ClassBox;
import BusinessLayer.Components.ClassDiagramComponents.ClassDiagramRelationship;
import BusinessLayer.Components.UMLComponent;
import BusinessLayer.Components.UseCaseDiagramComponents.UseCaseDiagramRelationship;
import ui.ConnectionDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class ClassDiagram extends UMLDiagram {

    public ClassDiagram() {
        components = new ArrayList<>();
        this.name = "";
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
        if (components.contains(component)) { // Remove only if present

            if(component instanceof ClassBox componentToRemove){ // safely deleting

                for(ClassDiagramRelationship relationship: componentToRemove.getRelationships()){

                    if(relationship.getTo() instanceof ClassBox otherClass && otherClass != componentToRemove){
                        // if 'to' is this class, don't delete relationship yet
                        otherClass.removeRelationship(relationship); // relationship was added to two classes, delete from other class first
                        components.remove(relationship); // remove from array
                        remove(relationship); // remove from diagram
                    }
                    else if(relationship.getFrom() instanceof ClassBox otherClass && otherClass != componentToRemove){
                        otherClass.removeRelationship(relationship);
                        components.remove(relationship);
                        remove(relationship);
                    }
                }

            } else if(component instanceof  ClassDiagramRelationship obj){
                obj.removeFromAndTo();
            }

            components.remove(component);
            remove(component);
            revalidate();
            repaint();
        }
    }


    @Override
    public void renderComponents(Graphics g) {

    }

    @Override
    protected void createConnection(UMLComponent comp1, UMLComponent comp2) {

        JFrame frame = new JFrame("Diagram");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(300, 300);
        //frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        ConnectionDialog dialog = new ConnectionDialog(frame, "Class");

        if(dialog.getOptionSelected().equalsIgnoreCase("Association")) {
            ClassDiagramRelationship relationship = new ClassDiagramRelationship(comp1, comp2, "Association");
            addComponent(relationship);
        } else if(dialog.getOptionSelected().equalsIgnoreCase("aggregation")) {
            ClassDiagramRelationship relationship = new ClassDiagramRelationship(comp1, comp2, "aggregation");
            addComponent(relationship);
        } else if(dialog.getOptionSelected().equalsIgnoreCase("composition")) {
            ClassDiagramRelationship relationship = new ClassDiagramRelationship(comp1, comp2, "composition");
            addComponent(relationship);
        } else if(dialog.getOptionSelected().equalsIgnoreCase("inheritance")) {
            ClassDiagramRelationship relationship = new ClassDiagramRelationship(comp1, comp2, "inheritance");
            addComponent(relationship);
        }

        revalidate();
        repaint();
    }
}