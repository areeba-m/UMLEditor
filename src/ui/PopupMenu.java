package ui;

import BusinessLayer.Components.UMLComponent;
import BusinessLayer.Diagrams.ClassDiagram;
import BusinessLayer.Diagrams.UMLDiagram;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import static BusinessLayer.Diagrams.UMLDiagram.components;

/**
 * Popup menu class extends JPopup for a customized menu option for relationships
 *
 */
public class PopupMenu extends JPopupMenu {

    private JMenuItem deleteItem;
    private JMenuItem associationItem;
    private JMenuItem inheritanceItem;
    private JMenuItem aggregationItem;
    private JMenuItem compositionItem;
    private JMenuItem includeItem;
    private JMenuItem extendItem;

    ImageIcon originalIcon;
    Image resizedImage;
    ImageIcon resizedIcon;

    JFrame frame;
    ConnectionDialog dialog;

    private UMLDiagram diagram;

    public PopupMenu(UMLDiagram diagram) {
        this.diagram = diagram;

        int newWidth = 30;
        int newHeight = 30;

        originalIcon = new ImageIcon("association.png");
        resizedImage = originalIcon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        resizedIcon = new ImageIcon(resizedImage);
        associationItem = new JMenuItem();
        associationItem.setIcon(resizedIcon);
        add(associationItem);

        frame = new JFrame("Diagram");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(300, 300);
        frame.setLocationRelativeTo(null);

        if(diagram instanceof ClassDiagram) {
            originalIcon = new ImageIcon("aggregation.png");
            resizedImage = originalIcon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            resizedIcon = new ImageIcon(resizedImage);
            aggregationItem = new JMenuItem();
            aggregationItem.setIcon(resizedIcon);
            add(aggregationItem);

            originalIcon = new ImageIcon("composition.png");
            resizedImage = originalIcon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            resizedIcon = new ImageIcon(resizedImage);
            compositionItem = new JMenuItem();
            compositionItem.setIcon(resizedIcon);
            add(compositionItem);

            originalIcon = new ImageIcon("inheritance.png");
            resizedImage = originalIcon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            resizedIcon = new ImageIcon(resizedImage);
            inheritanceItem = new JMenuItem();
            inheritanceItem.setIcon(resizedIcon);
            add(inheritanceItem);

            inheritanceItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    handleSelection(e, "Inheritance");
                }
            });

            aggregationItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    handleSelection(e, "Aggregation");
                }
            });

            compositionItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    handleSelection(e, "Composition");
                }
            });

        } else {
            originalIcon = new ImageIcon("include.png");
            resizedImage = originalIcon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            resizedIcon = new ImageIcon(resizedImage);
            includeItem = new JMenuItem();
            includeItem.setIcon(resizedIcon);
            add(includeItem);

            originalIcon = new ImageIcon("extend.png");
            resizedImage = originalIcon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            resizedIcon = new ImageIcon(resizedImage);
            extendItem = new JMenuItem();
            extendItem.setIcon(resizedIcon);
            add(extendItem);


            includeItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    handleSelection(e, "Include");
                }
            });

            extendItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    handleSelection(e, "Extend");
                }
            });
        }

        originalIcon = new ImageIcon("recycle-bin.png");
        resizedImage = originalIcon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        resizedIcon = new ImageIcon(resizedImage);
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

        associationItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSelection(e, "Association");
            }
        });

    }

    public void handleSelection(ActionEvent e, String type){

        UMLComponent selectedComponent = diagram.getSelectedComponent();
        if (selectedComponent != null) {
            dialog = new ConnectionDialog(frame, selectedComponent.getName(), components);
            UMLComponent otherSelectedComponent = dialog.getSelectedComponent();

            if(otherSelectedComponent!=null){
                int result = diagram.createConnection(selectedComponent,otherSelectedComponent, type);

                if(result == 1){
                    JOptionPane.showMessageDialog(this,
                            "Association between UseCases is an invalid notation.", "Create Relationship", JOptionPane.WARNING_MESSAGE
                    );
                } else if (result == 2){
                    JOptionPane.showMessageDialog(null,
                            "Include between UseCase and Actor is an invalid notation.", "Create Relationship", JOptionPane.WARNING_MESSAGE
                    );
                } else if (result == 3){
                    JOptionPane.showMessageDialog(null,
                            "Extend between UseCase and Actor is an invalid notation.", "Create Relationship", JOptionPane.WARNING_MESSAGE
                    );
                } else if (result == 4){
                    JOptionPane.showMessageDialog(null,
                            type + " between selected components is invalid.", "Create Relationship", JOptionPane.WARNING_MESSAGE
                    );
                } else if (result == 5){
                    JOptionPane.showMessageDialog(null,
                            "We do not support self-relationships :(", "Create Relationship", JOptionPane.WARNING_MESSAGE
                    );
                }


            } else {
                JOptionPane.showMessageDialog(null,
                        "Other component was not selected", "Create Relationship", JOptionPane.WARNING_MESSAGE
                );
            }
            setVisible(false);
        }


    }

}
