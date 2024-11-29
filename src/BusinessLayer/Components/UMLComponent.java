package BusinessLayer.Components;

import BusinessLayer.Components.UseCaseDiagramComponents.Actor;
import BusinessLayer.Components.UseCaseDiagramComponents.UseCase;
import BusinessLayer.Components.UseCaseDiagramComponents.UseCaseDiagramRelationship;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class UMLComponent extends JComponent {
    protected String name;
    protected Point point;

    protected Point dragOffset;
    private boolean isSelected = false;

    public UMLComponent() {
        // Add mouse listeners for drag functionality
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Store the offset where the mouse was clicked
                dragOffset = e.getPoint();
                setSelected(true);

                if(UMLComponent.this instanceof UseCaseDiagramRelationship){
                    UseCaseDiagramRelationship obj = (UseCaseDiagramRelationship)UMLComponent.this;
                    obj.handleUseCaseRelationshipMousePressed(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e){
                setSelected(false);
                if(UMLComponent.this instanceof UseCaseDiagramRelationship){
                    UseCaseDiagramRelationship obj = (UseCaseDiagramRelationship)UMLComponent.this;
                    obj.handleUseCaseRelationshipMouseReleased(e);
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // Update the component's location based on mouse movement
                if(UMLComponent.this instanceof UseCase || UMLComponent.this instanceof Actor) {


                }
                Point newLocation = getParent().getMousePosition();
                if (newLocation != null) {
                    setLocation(newLocation.x - dragOffset.x, newLocation.y - dragOffset.y);
                    getParent().revalidate();
                    getParent().repaint();
                }
                if(UMLComponent.this instanceof UseCaseDiagramRelationship){
                    UseCaseDiagramRelationship obj = (UseCaseDiagramRelationship)UMLComponent.this;
                    obj.handleUseCaseRelationshipMouseDragged(e);
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