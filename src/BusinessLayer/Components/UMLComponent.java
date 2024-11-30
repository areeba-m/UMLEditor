package BusinessLayer.Components;

import BusinessLayer.Components.UseCaseDiagramComponents.Actor;
import BusinessLayer.Components.UseCaseDiagramComponents.UseCase;
import BusinessLayer.Components.UseCaseDiagramComponents.UseCaseDiagramRelationship;
import ui.UMLEditorForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

import static BusinessLayer.Diagrams.UMLDiagram.components;

public abstract class UMLComponent extends JComponent {
    protected String name;
    protected Point point;

    protected Point dragOffset;
    private boolean isSelected = false;

    protected static JTextArea textArea;

    public UMLComponent() {

        textArea = UMLEditorForm.getTextArea();

        // Add mouse listeners for drag functionality
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                dragOffset = e.getPoint();
                for (UMLComponent component : components) {
                    component.setSelected(false);
                }
                if (UMLComponent.this.contains(e.getPoint())) {
                    UMLComponent.this.setSelected(true); // Select only this component
                }

                if(UMLComponent.this instanceof UseCaseDiagramRelationship obj){
                    obj.handleUseCaseRelationshipMousePressed(e);
                }
                else if(UMLComponent.this instanceof UseCase obj){
                    obj.handleUseCaseMousePressed(e);
                }

                textArea.setText(getName());
                textArea.requestFocus();
            }

            @Override
            public void mouseReleased(MouseEvent e){

                boolean clickedOnComponent = false;
                // Check if the click is on any component
                for (UMLComponent component : components) {
                    if (component.contains(e.getPoint())) {
                        clickedOnComponent = true;
                        break;
                    }
                }
                // If no component was clicked, deselect all
                if (!clickedOnComponent) {
                    for (UMLComponent component : components) {
                        component.setSelected(false);
                    }
                }

                if(UMLComponent.this instanceof UseCaseDiagramRelationship obj){
                    obj.handleUseCaseRelationshipMouseReleased(e);
                }
                else if(UMLComponent.this instanceof UseCase obj){
                    obj.handleUseCaseMouseReleased(e);
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {

                if(UMLComponent.this instanceof UseCaseDiagramRelationship){
                    UseCaseDiagramRelationship obj = (UseCaseDiagramRelationship)UMLComponent.this;
                    obj.handleUseCaseRelationshipMouseDragged(e);

                    if(((UseCaseDiagramRelationship)UMLComponent.this).isDraggingStart() ||
                            ((UseCaseDiagramRelationship)UMLComponent.this).isDraggingEnd()){
                        return; // if its being dragged, do not move it around
                    }
                }
                else if(UMLComponent.this instanceof UseCase obj){
                    obj.handleUseCaseMouseDragged(e);

                }

                Point newLocation = getParent().getMousePosition();
                if (newLocation != null) {
                    setLocation(newLocation.x - dragOffset.x, newLocation.y - dragOffset.y);
                    getParent().revalidate();
                    getParent().repaint();
                }

            }
        });
    }

    public abstract void draw(Graphics g);

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
        repaint();
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
        repaint();
    }

}