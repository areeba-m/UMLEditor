package ui;

import BusinessLayer.Components.UMLComponent;
import BusinessLayer.Diagrams.UMLDiagram;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseEvent;


public class PopupMenu extends JPopupMenu {

    private JMenuItem deleteItem;
    private UMLDiagram diagram;

    public PopupMenu(UMLDiagram diagram) {
        this.diagram = diagram;

        ImageIcon originalIcon = new ImageIcon("recycle-bin.png");

        int newWidth = 30;
        int newHeight = 30;
        Image resizedImage = originalIcon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        deleteItem = new JMenuItem();
        deleteItem.setIcon(resizedIcon);
        add(deleteItem);

        deleteItem.addActionListener(e -> {
            UMLComponent selectedComponent = diagram.getSelectedComponent();
            if (selectedComponent != null) {
                diagram.removeComponent(selectedComponent);
                setVisible(false);
            }

        });
    }


}
