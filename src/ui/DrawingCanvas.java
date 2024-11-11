package ui;

import BusinessLayer.Components.UseCaseDiagramComponents.UseCase;

import javax.swing.*;
import java.awt.*;
import java.awt.dnd.*;

public class DrawingCanvas extends JPanel implements DropTargetListener {

    public DrawingCanvas(){
        setLayout(null);
        setPreferredSize(new Dimension(2000,2000));
        setBackground(Color.PINK);

        new DropTarget(this,this);
    }

    @Override
    public void drop(DropTargetDropEvent event) {
        try {
            event.acceptDrop(DnDConstants.ACTION_COPY);

            Point dropPoint = event.getLocation();
            UseCase useCase = new UseCase("Dropped UseCase");  // Create new component on drop
            useCase.setBounds(dropPoint.x, dropPoint.y, useCase.getPreferredSize().width, useCase.getPreferredSize().height);

            add(useCase);
            revalidate();
            repaint();

        } catch (Exception e) {
            // display error
        }
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
}
