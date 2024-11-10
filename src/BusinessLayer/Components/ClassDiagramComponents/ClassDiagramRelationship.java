package BusinessLayer.Components.ClassDiagramComponents;

import BusinessLayer.Components.UMLComponent;

import java.awt.*;

public class ClassDiagramRelationship extends UMLComponent {
    ClassBox classA;
    ClassBox classB;
    String name;//name of relationship example "uses", will be empty for composition and aggregation
    String type;//types: 1)association, composition, aggregation
    @Override
    public void draw(Graphics g) {

    }
}