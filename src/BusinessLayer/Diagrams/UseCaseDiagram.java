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

    private UMLComponent selectedComponent; // Track the currently selected component
    private Point offset; // Offset between the mouse and the top-left corner of the component

    public UseCaseDiagram(JPanel panelGrid){
        components = new ArrayList<>();

        UseCaseDiagramRelationship includeRelationship = new UseCaseDiagramRelationship(null, null, "Include");
        UseCaseDiagramRelationship excludeRelationship = new UseCaseDiagramRelationship(null, null, "Exclude");
        UseCaseDiagramRelationship associationRelationship = new UseCaseDiagramRelationship(null, null, "Association");

        UseCase sampleUsecase = new UseCase("UseCase");

        Actor sampleActor = new Actor("Actor");

        panelGrid.add(includeRelationship);
        panelGrid.add(excludeRelationship);
        panelGrid.add(associationRelationship);
        panelGrid.add(sampleUsecase);
        panelGrid.add(sampleActor);


        setLayout(null);
        setPreferredSize(new Dimension(2000,2000));
        setBackground(Color.PINK);

        new DropTarget(this,this);
        // Add mouse listeners for selecting and dragging components
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                selectComponent(e.getPoint());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                selectedComponent = null; // Deselect the component on release
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                moveComponent(e.getPoint());
            }
        });

    }

    @Override
    public void addComponent(UMLComponent component) {
        components.add(component);
        add(component);
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
    public void dragEnter(DropTargetDragEvent dtde) {

    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {

    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {

    }

    @Override
    public void dragExit(DropTargetEvent dte) {

    }
/*
    @Override
    public void drop(DropTargetDropEvent event) {
        try {
            // Accept the drop action
            event.acceptDrop(DnDConstants.ACTION_COPY);

            // Get the drop location
            Point dropPoint = event.getLocation();

            Object data = event.getTransferable().getTransferData(new DataFlavor(Actor.class, "Actor"));
            if (data instanceof Actor) {
                Actor actor = (Actor) data;

                // Set position of the dropped Actor
                actor.setBounds(dropPoint.x, dropPoint.y, actor.getPreferredSize().width, actor.getPreferredSize().height);

                // Add Actor to the UseCaseDiagram and refresh the UI
                add(actor);
                components.add(actor);
                revalidate();
                repaint();
            }

            //Object data = event.getTransferable().getTransferData(DataFlavor.stringFlavor);
            /*
            UMLComponent componentToAdd = null;

            if (data.equals("Actor")) {
                componentToAdd = new Actor("Dropped Actor");
            } else if (data.equals("UseCase")) {
                componentToAdd = new UseCase("Dropped UseCase");
            } else if (data.equals("Association")) {
                componentToAdd = new UseCaseDiagramRelationship(null, null, "Association");
            } else if (data.equals("Include")) {
                componentToAdd = new UseCaseDiagramRelationship(null, null, "Include");
            } else if (data.equals("Exclude")) {
                componentToAdd = new UseCaseDiagramRelationship(null, null, "Exclude");
            }

            // If a valid component was created, set its position and add it to the diagram
            if (componentToAdd != null) {
                componentToAdd.setBounds(
                        dropPoint.x,
                        dropPoint.y,
                        componentToAdd.getPreferredSize().width,
                        componentToAdd.getPreferredSize().height
                );

                // Add the component to the diagram and the components list
                add(componentToAdd);
                components.add(componentToAdd);*

                // Refresh the UI
                revalidate();
                repaint();
            //}

            // Indicate the drop was successful
            event.dropComplete(true);

        } catch (Exception e) {
            // Handle any errors (e.g., invalid data format or type)
            e.printStackTrace();
            event.dropComplete(false);
        }
    }*/

    @Override
    public void drop(DropTargetDropEvent event) {
        try {
            // Accept the drop action
            event.acceptDrop(DnDConstants.ACTION_COPY);

            // Get the transfer data
            Object data = event.getTransferable().getTransferData(new DataFlavor(UseCaseComponent.class, "UseCaseComponent"));

            if (data instanceof UseCaseComponent) {
                UseCaseComponent component = (UseCaseComponent) data;

                // Set the position for the dropped component
                Point dropPoint = event.getLocation();
                component.setBounds(
                        dropPoint.x,
                        dropPoint.y,
                        component.getPreferredSize().width,
                        component.getPreferredSize().height
                );

                // Add the component to the UseCaseDiagram
                addComponent(component);
            }

            event.dropComplete(true);
        } catch (Exception e) {
            e.printStackTrace();
            event.dropComplete(false);
        }
    }

    /**
     * Select a component at the given point.
     */
    private void selectComponent(Point point) {
        for (UMLComponent component : components) {
            if (component.getBounds().contains(point)) {
                selectedComponent = component;
                offset = new Point(point.x - component.getX(), point.y - component.getY());
                return;
            }
        }
        selectedComponent = null; // Deselect if no component is clicked
    }

    /**
     * Move the selected component based on the new mouse position.
     */
    private void moveComponent(Point point) {
        if (selectedComponent != null) {
            int newX = point.x - offset.x;
            int newY = point.y - offset.y;

            selectedComponent.setBounds(newX, newY, selectedComponent.getWidth(), selectedComponent.getHeight());
            repaint(); // Refresh the diagram to reflect the move
        }
    }
}

