package BusinessLayer.Diagrams;

import BusinessLayer.Components.ClassDiagramComponents.ClassBox;
import BusinessLayer.Components.ClassDiagramComponents.ClassDiagramRelationship;
import BusinessLayer.Components.ClassDiagramComponents.Inheritence;
import BusinessLayer.Components.UMLComponent;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ClassDiagram extends UMLDiagram {
    private JPanel panel;

    public ClassDiagram(JPanel gridPanel) {
        this.panel = new JPanel();
        this.panel = gridPanel;
        this.components = new ArrayList<>();
        this.name = "";

        int startX = 10;
        int startY = 10;
        int offsetX = 55;
        int offsetY = 50;

        // Create and position each ClassBox
        ClassBox simpleClass = new ClassBox("SimpleClass", "Simple");
        simpleClass.getPoint().setLocation(startX, startY);

        ClassBox abstractClass = new ClassBox("AbstractClass", "Abstract");
        abstractClass.getPoint().setLocation(startX + offsetX, startY);

        ClassBox interfaceClass = new ClassBox("ClassName", "Interface");
        interfaceClass.getPoint().setLocation(startX, startY + offsetY);

        // Add boxes to the panel
        gridPanel.add(simpleClass);
        gridPanel.add(abstractClass);
        gridPanel.add(interfaceClass);

        // Create relationship lines beneath the AbstractClass
        int relationshipStartX = abstractClass.getPoint().x + offsetX / 2;
        int relationshipStartY = abstractClass.getPoint().y + 50; // Below the abstract class

        // Define and draw relationships
        ClassDiagramRelationship association = new ClassDiagramRelationship(
                new Point(relationshipStartX, relationshipStartY+5), // Start at AbstractClass
                new Point(relationshipStartX + 100, relationshipStartY+5) // End at some point (adjust as needed)
        );
        association.setType("association");

        ClassDiagramRelationship aggregation = new ClassDiagramRelationship(
                new Point(relationshipStartX, relationshipStartY + 20), // Start below the association line
                new Point(relationshipStartX + 100, relationshipStartY + 20) // End at some point
        );
        aggregation.setType("aggregation");

        ClassDiagramRelationship composition = new ClassDiagramRelationship(
                new Point(relationshipStartX, relationshipStartY + 35), // Start below the aggregation line
                new Point(relationshipStartX + 100, relationshipStartY + 35) // End at some point
        );
        composition.setType("composition");

        Inheritence inheritance = new Inheritence(
                new Point(relationshipStartX, relationshipStartY + 50), // Start below the composition line
                new Point(relationshipStartX + 100, relationshipStartY + 50) // End at some point
        );

        // Add the relationships to the panel (they will be drawn by their paintComponent method)
        gridPanel.add(association);
        gridPanel.add(aggregation);
        gridPanel.add(composition);
        gridPanel.add(inheritance);
    }


    @Override
    public void addComponent(UMLComponent component) {
        this.components.add(component);
    }

    @Override
    public void removeComponent(UMLComponent component) {

    }

    @Override
    public void renderComponents(Graphics g) {

    }
}