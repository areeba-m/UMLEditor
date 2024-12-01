package ui;

import BusinessLayer.Components.ClassDiagramComponents.ClassBox;
import BusinessLayer.Components.ClassDiagramComponents.ClassDiagramRelationship;
import BusinessLayer.Components.UMLComponent;
import BusinessLayer.Diagrams.ClassDiagram;
import BusinessLayer.Diagrams.UMLDiagram;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class UMLEditorForm extends JFrame {

    // menu bar components
    JMenuBar menuBar;

    JMenu fileMenu;
    JMenu exportMenu;
    JMenu codeMenu;

    JMenuItem saveProject;
    JMenuItem loadProject;
    JMenuItem exportPng;
    JMenuItem exportJpeg;
    JMenuItem generateCodeFiles;

    // canvas components
    //DrawingCanvas drawingCanvas;
    JScrollPane canvasScrollPane;

    //Diagram
    UMLDiagram workingDiagram;

    // diagram components
    JPanel panelEast;
    JComboBox<String> cmbDiagramType;
    JPanel panelComboBox;
    JPanel panelGrid;
    JPanel panelTopEast;
    JScrollPane diagramTypeScrollPane;

    // text area components
    static JTextArea textArea;
    ClassBox selectedClassBox;
    JScrollPane textAreaScrollPane;
    JPanel panelBottomEast;
    JButton updateTextArea;

    // grid components
    JPanel umlClassPanel;
    JPanel umlUseCasePanel;


    public UMLEditorForm(){

        setTitle("UML Editor Application");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        workingDiagram = new ClassDiagram();

        setUIFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));

        prepareMenuBar();
        prepareDiagramType();
        prepareCanvas(workingDiagram);
        loadGrid();
        pack();

//        textArea.getDocument().addDocumentListener(new DocumentListener() {
//            @Override
//            public void insertUpdate(DocumentEvent e) {
//                getAttributesAndMethods();
//            }
//
//            @Override
//            public void removeUpdate(DocumentEvent e) {
//                getAttributesAndMethods();
//            }
//
//            @Override
//            public void changedUpdate(DocumentEvent e) {
//                getAttributesAndMethods();
//            }
//        });
        updateTextArea.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                updateUMLComponent();
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        saveProject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Save Project");
                int userSelection = fileChooser.showSaveDialog(UMLEditorForm.this);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    String fileName = fileChooser.getSelectedFile().toString();
                    if (!fileName.endsWith(".json")) {
                        fileName += ".json";
                    }
                    try {
                        workingDiagram.saveToFile(fileName);
                        JOptionPane.showMessageDialog(UMLEditorForm.this, "Project saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(UMLEditorForm.this, "Failed to save the project: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        loadProject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Load Project");
                int userSelection = fileChooser.showSaveDialog(UMLEditorForm.this);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    String fileName = fileChooser.getSelectedFile().toString();
                    if (!fileName.endsWith(".json")) {
                        fileName += ".json";
                    }
                    try {
                        UMLDiagram diagram = workingDiagram.loadFromFile(fileName);
                        // Check if the loaded diagram is a ClassDiagram
                        if (diagram instanceof ClassDiagram) {
                            // If it is a ClassDiagram, populate workingDiagram
                            workingDiagram = (ClassDiagram) diagram;
                            String name = workingDiagram.getName();
                            // Initialize the ClassBox components for the workingDiagram
                            ArrayList<UMLComponent> components = workingDiagram.getListOfComponents();
                            workingDiagram = new ClassDiagram();
                            workingDiagram.setName(name);
                            // Assuming UMLDiagram has a getComponents method
                            for (UMLComponent component : components) {
                                if (component instanceof ClassBox) {
                                    ClassBox classBox = new ClassBox();
                                    classBox.setName(component.getName());
                                    classBox.setType(component.getType());
                                    classBox.setHeight(component.getHeight());
                                    classBox.setWidth(component.getWidth());
                                    classBox.setAttributes(((ClassBox) component).getAttributes());
                                    classBox.setMethods(((ClassBox) component).getMethods());
                                    classBox.setSelected(false);
                                    //workingDiagram.setupComponentForDiagram(classBox);
                                    workingDiagram.addComponent(classBox);
                                }
                            }
                            loadComponentsForSelectedDiagram("UML Class");
                            //canvasScrollPane.add(workingDiagram);
                            // Redraw the diagram after loading
                            //workingDiagram.repaint();
                            prepareCanvas(workingDiagram);
                            JOptionPane.showMessageDialog(UMLEditorForm.this, "Project loaded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(UMLEditorForm.this, "The loaded file is not a Class Diagram.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(UMLEditorForm.this, "Failed to save the project: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

    }

    private void updateUMLComponent() {
        for(int i = 0; i < workingDiagram.getComponentsCount(); i++)
        {
             if(workingDiagram.getComponentAt(i).isSelected())
            {
                workingDiagram.getComponentAt(i).updateFromTextArea();
                workingDiagram.getComponentAt(i).setSelected(false);
                workingDiagram.repaint();
                break;
            }
        }
    }

    private void prepareMenuBar(){

        menuBar = new JMenuBar();

        fileMenu = new JMenu("\uD83D\uDCC1 File");
        saveProject = new JMenuItem("\uD83D\uDCBE Save Project");
        loadProject = new JMenuItem("\uD83D\uDCE5 Load Project");
        fileMenu.add(saveProject);
        fileMenu.add(loadProject);

        exportMenu = new JMenu("\uD83C\uDF10 Export");
        exportPng = new JMenuItem("...to PNG");
        exportJpeg = new JMenuItem("...to JPEG");
        exportMenu.add(exportPng);
        exportMenu.add(exportJpeg);

        codeMenu = new JMenu(" \uD83D\uDEE0\uFE0F Code");
        generateCodeFiles = new JMenuItem("⚙\uFE0F Generate Code Files");
        codeMenu.add(generateCodeFiles);

        menuBar.add(fileMenu);
        menuBar.add(exportMenu);
        menuBar.add(codeMenu);

        setJMenuBar(menuBar);
    }

    private void prepareCanvas(UMLDiagram diagram){
        workingDiagram = diagram;
        if(canvasScrollPane != null)
        {
            this.remove(canvasScrollPane);
        }
        canvasScrollPane = new JScrollPane(workingDiagram);
        canvasScrollPane.setPreferredSize(new Dimension(800,600));
        add(canvasScrollPane, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void prepareDiagramType(){
        String[] type = {"UML Class", "UML Use Case"};
        cmbDiagramType = new JComboBox<>(type);

        panelComboBox = new JPanel();
        panelComboBox.setLayout(new BoxLayout(panelComboBox, BoxLayout.Y_AXIS));
        panelComboBox.add(cmbDiagramType);

        panelGrid = new JPanel(new GridLayout(0,2));
        panelGrid.setPreferredSize(new Dimension(1000,1000));

        diagramTypeScrollPane = new JScrollPane(panelGrid);

        panelTopEast = new JPanel();
        panelTopEast.setLayout(new BoxLayout(panelTopEast, BoxLayout.Y_AXIS));
        panelTopEast.setPreferredSize(new Dimension(400, 300));
        panelTopEast.add(panelComboBox);
        panelTopEast.add(Box.createVerticalGlue());
        panelTopEast.add(diagramTypeScrollPane);

        // initialize text area
        textArea = new JTextArea();
        textArea.setText("Add diagram notes");
        textArea.setPreferredSize(new Dimension(500,500));

        textAreaScrollPane = new JScrollPane(textArea);

        panelBottomEast = new JPanel();
        panelBottomEast.setPreferredSize(new Dimension(400,300));
        panelBottomEast.setLayout(new BoxLayout(panelBottomEast, BoxLayout.Y_AXIS));
        panelBottomEast.add(textAreaScrollPane);

        updateTextArea = new JButton("Update");

        // combine panels
        panelEast = new JPanel();
        panelEast.setLayout(new BoxLayout(panelEast, BoxLayout.Y_AXIS));
        panelEast.setPreferredSize(new Dimension(400, 600));
        panelEast.add(panelTopEast);
        panelEast.add(panelBottomEast);
        panelEast.add(updateTextArea);

        add(panelEast, BorderLayout.EAST);
    }

    public static void setUIFont(Font font) {
        java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, new javax.swing.plaf.FontUIResource(font));
            }
        }
    }

    private void loadGrid(){
        cmbDiagramType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String diagramType = (String) cmbDiagramType.getSelectedItem();
                loadComponentsForSelectedDiagram(diagramType);

            }
        });
    }

    private void loadComponentsForSelectedDiagram(String diagramType) {
        panelGrid.removeAll(); // Clear grid area before adding new components

        if ("UML Class".equals(diagramType))
        {
            if(!(workingDiagram instanceof ClassDiagram)) {
                workingDiagram = new ClassDiagram();
            }
            int startX = 10;
            int startY = 10;
            int offsetX = 55;
            int offsetY = 50;

            // Create and position each ClassBox
            ClassBox simpleClass = new ClassBox();
            simpleClass.setName("SimpleClass");
            simpleClass.setType("simple");
            simpleClass.getPoint().setLocation(startX, startY);
            simpleClass.setIsGridPanel(true);
            simpleClass.setSelected(false);

            ClassBox abstractClass = new ClassBox();
            abstractClass.setName("AbstractClass");
            abstractClass.setType("Abstract");
            abstractClass.getPoint().setLocation(startX + offsetX, startY);
            abstractClass.setIsGridPanel(true);
            abstractClass.setSelected(false);

            ClassBox interfaceClass = new ClassBox();
            interfaceClass.setName("Class");
            interfaceClass.setType("Interface");
            interfaceClass.getPoint().setLocation(startX, startY + offsetY);
            interfaceClass.setIsGridPanel(true);
            interfaceClass.setSelected(false);

            // Create relationship lines beneath the AbstractClass
            int relationshipStartX = abstractClass.getPoint().x + offsetX / 2;
            int relationshipStartY = abstractClass.getPoint().y + 50; // Below the abstract class

            // Define and draw relationships
            ClassDiagramRelationship association = new ClassDiagramRelationship(
                    new Point(relationshipStartX, relationshipStartY+5), // Start at AbstractClass
                    new Point(relationshipStartX + 100, relationshipStartY+5) // End at some point (adjust as needed)
            );
            association.setType("association");
            association.setSelected(false);
            association.setIsGridPanel(true);

            ClassDiagramRelationship aggregation = new ClassDiagramRelationship(
                    new Point(relationshipStartX, relationshipStartY + 20), // Start below the association line
                    new Point(relationshipStartX + 100, relationshipStartY + 20) // End at some point
            );
            aggregation.setType("aggregation");
            aggregation.setSelected(false);
            aggregation.setIsGridPanel(true);

            ClassDiagramRelationship composition = new ClassDiagramRelationship(
                    new Point(relationshipStartX, relationshipStartY + 35), // Start below the aggregation line
                    new Point(relationshipStartX + 100, relationshipStartY + 35) // End at some point
            );
            composition.setType("composition");
            composition.setSelected(false);
            composition.setIsGridPanel(true);

            ClassDiagramRelationship inheritance = new ClassDiagramRelationship(
                    new Point(relationshipStartX, relationshipStartY + 50), // Start below the composition line
                    new Point(relationshipStartX + 100, relationshipStartY + 50) // End at some point
            );
            inheritance.setType("inheritence");
            inheritance.setSelected(false);
            inheritance.setIsGridPanel(true);

            setupComponentForGrid(simpleClass);
            setupComponentForGrid(abstractClass);
            setupComponentForGrid(interfaceClass);
            setupComponentForGrid(inheritance);
            setupComponentForGrid(association);
            setupComponentForGrid(composition);
            setupComponentForGrid(aggregation);

            // Add boxes to the panel
            panelGrid.add(simpleClass);
            panelGrid.add(abstractClass);
            panelGrid.add(interfaceClass);

            // Add the relationships to the panel (they will be drawn by their paintComponent method)
            panelGrid.add(inheritance);
            panelGrid.add(association);
            panelGrid.add(aggregation);
            panelGrid.add(composition);
        }
        else if ("UML Use Case".equals(diagramType))
        {
        }

        panelGrid.revalidate();
        panelGrid.repaint();
    }

    private void setupComponentForGrid(UMLComponent component) {
        component.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                try {
                    UMLComponent newComponent = null;
                    if (component instanceof ClassBox) {
                        newComponent = new ClassBox();
                        newComponent.setName(component.getName());
                        newComponent.setType(component.getType());
                        newComponent.setIsDropped(true);//component is now dropped to the canvas
                        newComponent.setSelected(false);
                    }
                    else if(component instanceof ClassDiagramRelationship)
                    {
                        Point end = new Point((int) (e.getPoint().getX() + 30), (int) (e.getPoint().getY()+0));
                        newComponent = new ClassDiagramRelationship(e.getPoint(), end);
                    }

                    workingDiagram.setupComponentForDiagram(newComponent); // Set drag listeners
                    workingDiagram.addComponent(newComponent);
                    //workingDiagram.repaint();

                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }

            }
        });
    }

    public static void main(String[] args){
        UMLEditorForm app = new UMLEditorForm();
        app.setVisible(true);
    }
    public void setSelectedClassBox(ClassBox classBox) {
        selectedClassBox = classBox;

    }
    public static JTextArea getTextArea()
    {
        return textArea;
    }
}
