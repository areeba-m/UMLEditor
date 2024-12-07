package ui;
import BusinessLayer.Components.UMLComponent;
import BusinessLayer.Components.ClassDiagramComponents.ClassBox;
import BusinessLayer.Components.ClassDiagramComponents.ClassDiagramRelationship;
import BusinessLayer.Components.UMLComponent;
import BusinessLayer.Components.UseCaseDiagramComponents.Actor;
import BusinessLayer.Components.UseCaseDiagramComponents.UseCase;
import BusinessLayer.Components.UseCaseDiagramComponents.UseCaseDiagramRelationship;
import BusinessLayer.Diagrams.ClassDiagram;
import BusinessLayer.Diagrams.UMLDiagram;
import BusinessLayer.Diagrams.UseCaseDiagram;
import Data.CodeGenerator;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

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
    JButton btnUpdate;

    // grid components
    JPanel umlClassPanel;
    JPanel umlUseCasePanel;
    // new feature in ui
    public static boolean isConnectMode;
    private SwitchButton switchButton;

    public UMLEditorForm(){

        setTitle("UML Editor Application");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        setUIFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));

        prepareMenuBar();
        prepareDiagramType();
        UMLDiagram diagram = new ClassDiagram();
        prepareCanvas(diagram);
        loadGrid();

        exportPng.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportPanelAsPNG(workingDiagram);
            }
        });
        exportJpeg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportPanelAsJPEG(workingDiagram);
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < workingDiagram.getComponentsCount(); i++) {
                    if (workingDiagram.getComponentAt(i).isSelected()) {
                        workingDiagram.getComponentAt(i).updateFromTextArea();
                        workingDiagram.getComponentAt(i).setSelected(false);
                        workingDiagram.repaint();
                        break;
                    }
                }
            }
        });

        textArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                updateComponentName();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                updateComponentName();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                updateComponentName();
            }

            private void updateComponentName() {
                ArrayList<UMLComponent> components = workingDiagram.components;

                for (UMLComponent component : components) {
                    if (!(component instanceof ClassBox || component instanceof ClassDiagramRelationship)
                            && component.isSelected()) {
                        component.setName(textArea.getText());
                    }
                }
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

                                    if (component.getBounds() != null) {
                                        classBox.setBounds(component.getBounds());
                                    }
                                    if (component.getBounds() != null) {
                                        classBox.setLocation(component.getBounds().x, component.getBounds().y);
                                    } else if (component instanceof ClassBox) {
                                        Point point = ((ClassBox) component).getPoint();
                                        classBox.setLocation(point.x, point.y);
                                    }
                                    classBox.setPoint(((ClassBox) component).getLocation());
                                    //classBox.updatePreferredSize();
                                    //workingDiagram.setupComponentForDiagram(classBox);
                                    workingDiagram.addComponents(classBox);
                                }
                                else if (component instanceof ClassDiagramRelationship) {
                                    ClassBox from = null;
                                    ClassBox to = null;
                                    for(int i = 0; i < workingDiagram.getComponentsCount(); i++)
                                    {
                                        if(workingDiagram.getComponentAt(i).getName().equals(((ClassDiagramRelationship) component).getFrom().getName()))
                                        {
                                            from = (ClassBox) ((ClassDiagramRelationship) component).getFrom();
                                            from.setBounds(((ClassDiagramRelationship) component).getFrom().getBounds());
                                        }
                                        if(workingDiagram.getComponentAt(i).getName().equals(((ClassDiagramRelationship) component).getTo().getName()))
                                        {
                                            to = (ClassBox) ((ClassDiagramRelationship) component).getTo();
                                            to.setBounds(((ClassDiagramRelationship) component).getTo().getBounds());
                                        }
                                    }
//                                    ClassBox from = (ClassBox)(((ClassDiagramRelationship) component).getFrom());
//
//                                    ClassBox to = (ClassBox)(((ClassDiagramRelationship) component).getTo());
                                     String relationshipName = component.getName();
//                                   // Point start = new Point(((ClassDiagramRelationship) component).getPoint());
//                                   // Point end = new Point(((ClassDiagramRelationship) component).getEndPoint());
//                                    // Create the relationship using the constructor
                                     ClassDiagramRelationship relationship = new ClassDiagramRelationship(from, to, relationshipName);
//
//                                    // Set other properties
                                     relationship.setPoint(((ClassDiagramRelationship) component).getPoint());
                                     relationship.setEndPoint(((ClassDiagramRelationship) component).getEndPoint());
//
//                                    if (component.getBounds() != null) {
//                                        relationship.setBounds(component.getBounds());
//                                    }
//////                                    // Set the location for the relationship
//                                        if (component.getBounds() != null) {
//                                        relationship.setLocation(component.getBounds().x, component.getBounds().y);
//                                    }
                                    relationship.updateBounds();
                                    workingDiagram.addComponents(relationship);

                                }
                            }
                            prepareCanvas(workingDiagram);
                            JOptionPane.showMessageDialog(UMLEditorForm.this, "Project loaded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        } else if (diagram instanceof UseCaseDiagram) {
                            // Cast the loaded diagram to UseCaseDiagram
                            workingDiagram = (UseCaseDiagram) diagram;
                            String name = workingDiagram.getName();

                            // Initialize the UseCaseDiagram with the loaded components
                            ArrayList<UMLComponent> components = workingDiagram.getListOfComponents();
                            workingDiagram = new UseCaseDiagram();
                            workingDiagram.setName(name);

                            for (UMLComponent component : components) {
                                if (component instanceof Actor) {
                                    // Populate Actor components
                                    Actor actor = new Actor(component.getName());
                                    actor.setBounds(component.getBounds());
                                    actor.setLocation(component.getBounds().x, component.getBounds().y);
                                    workingDiagram.addComponents(actor);
                                } else if (component instanceof UseCase) {
                                    // Populate UseCase components
                                    UseCase useCase = new UseCase(component.getName());
                                    useCase.setBounds(component.getBounds());
                                    useCase.setLocation(component.getBounds().x, component.getBounds().y);
                                    workingDiagram.addComponents(useCase);
                                } else if (component instanceof UseCaseDiagramRelationship) {
                                    // Populate relationships
                                    UMLComponent fromComponent = null;
                                    UMLComponent toComponent = null;

                                    // Find `from` and `to` components by name in the existing diagram
                                    for (int i = 0; i < workingDiagram.getComponentsCount(); i++) {
                                        UMLComponent current = workingDiagram.getComponentAt(i);
                                        if (current.getName().equals(((UseCaseDiagramRelationship) component).getFrom().getName())) {
                                            fromComponent = current;
                                        }
                                        if (current.getName().equals(((UseCaseDiagramRelationship) component).getTo().getName())) {
                                            toComponent = current;
                                        }
                                    }

                                    // Create the relationship
                                    if (fromComponent != null && toComponent != null) {
                                        UseCaseDiagramRelationship relationship = new UseCaseDiagramRelationship(fromComponent, toComponent, component.getName());
                                        relationship.setName(component.getName());
                                        relationship.setLabel(((UseCaseDiagramRelationship) component).getLabel());
                                        relationship.setBounds(component.getBounds());
                                        relationship.setStartPoint(((UseCaseDiagramRelationship) component).getStartPoint());
                                        relationship.setEndPoint(((UseCaseDiagramRelationship) component).getEndPoint());
                                        relationship.updateBounds();
                                        workingDiagram.addComponents(relationship);
                                    }
                                }
                            }

                            prepareCanvas(workingDiagram);
                            JOptionPane.showMessageDialog(UMLEditorForm.this, "Use Case Diagram loaded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        }
                        else {
                            JOptionPane.showMessageDialog(UMLEditorForm.this, "The loaded file is not a Class Diagram.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(UMLEditorForm.this, "Failed to save the project: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        pack();
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

        isConnectMode = false;
        switchButton = new SwitchButton();
        switchButton.setCustomToolTipText("Connect Mode");

        JPanel switchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        switchPanel.setOpaque(false);
        switchPanel.add(switchButton);

        switchButton.addActionListener(e -> {
            isConnectMode = switchButton.isOn();
            switchButton.setCustomToolTipText("Connect Mode: " + switchButton.getState());

        });
        generateCodeFiles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseDirectory(e);
            }
        });

        menuBar.add(fileMenu);
        menuBar.add(exportMenu);
        menuBar.add(codeMenu);
        menuBar.add(switchPanel);

        setJMenuBar(menuBar);
    }

    private void prepareCanvas(UMLDiagram diagram){
        workingDiagram = diagram;//new UseCaseDiagram();
        if(canvasScrollPane != null){
            this.remove(canvasScrollPane);
        }
        canvasScrollPane = new JScrollPane(workingDiagram);
        canvasScrollPane.setPreferredSize(new Dimension(800,600));
        System.out.println("New working diagram added to scroll pane");
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

        panelGrid = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                g.setColor(Color.WHITE);
                g.fillRect(0, 0, getWidth(), getHeight());

                drawDottedBackground(g);
            }

            private void drawDottedBackground(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(211, 211, 211));

                int dotSize = 2;       // Diameter of each dot (small-sized)
                int dotSpacing = 15;   // Space between dots

                for (int x = 0; x < getWidth(); x += dotSpacing) {
                    for (int y = 0; y < getHeight(); y += dotSpacing) {
                        g2d.fillOval(x, y, dotSize, dotSize);
                    }
                }
            }
        };
        panelGrid.setPreferredSize(new Dimension(1000,1000));
        panelGrid.setBackground(Color.gray);
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

        btnUpdate = new JButton("↻ Update");

        textAreaScrollPane = new JScrollPane(textArea);

        JPanel panelForUpdateButton = new JPanel();
        panelForUpdateButton.setLayout(new BoxLayout(panelForUpdateButton, BoxLayout.X_AXIS));
        panelForUpdateButton.add(Box.createHorizontalGlue());
        panelForUpdateButton.add(btnUpdate);
        panelForUpdateButton.add(Box.createHorizontalStrut(10));

        panelBottomEast = new JPanel();
        panelBottomEast.setPreferredSize(new Dimension(400,300));
        panelBottomEast.setLayout(new BoxLayout(panelBottomEast, BoxLayout.Y_AXIS));
        panelBottomEast.add(textAreaScrollPane);
        panelBottomEast.add(panelForUpdateButton);

        // combine panels
        panelEast = new JPanel();
        panelEast.setLayout(new BoxLayout(panelEast, BoxLayout.Y_AXIS));
        panelEast.setPreferredSize(new Dimension(400, 600));
        panelEast.add(panelTopEast);
        panelEast.add(panelBottomEast);

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
                prepareCanvas(new ClassDiagram());
            }
            int startX = 10;
            int startY = 10;
            int offsetX = 190;
            int offsetY = 140;

            // Create and position each ClassBox
            ClassBox simpleClass = new ClassBox();
            simpleClass.setName("SimpleClass");
            simpleClass.setType("simple");
            //simpleClass.getPoint().setLocation(startX, startY);
            simpleClass.setIsGridPanel(true);
            simpleClass.setSelected(false);

            ClassBox concreteClass = new ClassBox();
            concreteClass.setName("ConcreteClass");
            concreteClass.setType("simple");
            //concreteClass.getPoint().setLocation(startX+offsetX, startY+offsetY);
            concreteClass.setIsGridPanel(true);
            concreteClass.setSelected(false);

            ClassBox abstractClass = new ClassBox();
            abstractClass.setName("AbstractClass");
            abstractClass.setType("Abstract");
            //abstractClass.getPoint().setLocation(startX + offsetX, startY);
            abstractClass.setIsGridPanel(true);
            abstractClass.setSelected(false);

            ClassBox interfaceClass = new ClassBox();
            interfaceClass.setName("Class");
            interfaceClass.setType("Interface");
            //interfaceClass.getPoint().setLocation(startX, startY + offsetY);
            interfaceClass.setIsGridPanel(true);
            interfaceClass.setSelected(false);

            // Define and draw relationships
            ClassDiagramRelationship association = new ClassDiagramRelationship(
                    simpleClass,
                    abstractClass,
                    "association"
            );
            association.setSelected(false);
            association.setIsGridPanel(true);

            ClassDiagramRelationship aggregation = new ClassDiagramRelationship(
                    abstractClass,
                    simpleClass,
                    "aggregation"
            );
            aggregation.setSelected(false);
            aggregation.setIsGridPanel(true);

            ClassDiagramRelationship composition = new ClassDiagramRelationship(
                    interfaceClass,
                    simpleClass,
                    "composition"
            );
            composition.setSelected(false);
            composition.setIsGridPanel(true);

            ClassDiagramRelationship inheritance = new ClassDiagramRelationship(
                    abstractClass,
                    concreteClass,
                    "inheritance"
            );
            inheritance.setSelected(false);
            inheritance.setIsGridPanel(true);

            simpleClass.setBounds(10, 10, 100, 70);
            abstractClass.setBounds(200, 10, 100, 70);
            interfaceClass.setBounds(10, 150, 100, 100);
            concreteClass.setBounds(200, 150, 100, 70);

            setupComponentForGrid(simpleClass);
            setupComponentForGrid(abstractClass);
            setupComponentForGrid(interfaceClass);
            setupComponentForGrid(concreteClass);

            panelGrid.add(simpleClass);
            panelGrid.add(abstractClass);
            panelGrid.add(interfaceClass);
            panelGrid.add(concreteClass);
//            panelGrid.add(inheritance);
//            panelGrid.add(association);
//            panelGrid.add(aggregation);
//            panelGrid.add(composition);

        } else if ("UML Use Case".equals(diagramType)) {
            if (!(workingDiagram instanceof UseCaseDiagram)) {
                prepareCanvas(new UseCaseDiagram());
            }

            UseCase uc1 = new UseCase("UseCase 1");
            UseCase uc2 = new UseCase("UseCase 2");
            UseCase uc3 = new UseCase("UseCase 3");
            Actor actor = new Actor("Actor");

            actor.setBounds(10, 10, 50, 100);
            uc1.setBounds(150, 10, 100, 50);
            uc2.setBounds(250, 100, 100, 50);
            uc3.setBounds(120, 180, 100, 50);

            UseCaseDiagramRelationship includeRelationship = new UseCaseDiagramRelationship(uc3, uc2, "Include");
            UseCaseDiagramRelationship extendRelationship = new UseCaseDiagramRelationship(uc1, uc2, "extend");
            UseCaseDiagramRelationship associationRelationship = new UseCaseDiagramRelationship(actor, uc1, "Association");

            includeRelationship.setManualBounds(new Point(125, 165), new Point(275, 365));
            extendRelationship.setManualBounds(new Point(125, 145), new Point(375, 345));
            associationRelationship.setManualBounds(new Point(100, 50), new Point(150, 100));

            setupComponentForGrid(uc1);
            setupComponentForGrid(uc2);
            setupComponentForGrid(uc3);
            setupComponentForGrid(actor);

            panelGrid.add(uc1);
            panelGrid.add(uc2);
            panelGrid.add(uc3);
            panelGrid.add(actor);
            panelGrid.add(associationRelationship);
            panelGrid.add(includeRelationship);
            panelGrid.add(extendRelationship);
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
                        newComponent.setIsGridPanel(false);
                    }
                    else if(component instanceof ClassDiagramRelationship)
                    {
//                        Point end = new Point((int) (e.getPoint().getX() + 30), (int) (e.getPoint().getY()+0));
//                        newComponent = new ClassDiagramRelationship(e.getPoint(), end);
                    } else if (component instanceof Actor) {
                        newComponent = new Actor(component.getName());
                    } else if (component instanceof UseCase) {
                        newComponent = new UseCase(component.getName());
                    } else if (component instanceof UseCaseDiagramRelationship) {
                        // newComponent = new UseCaseDiagramRelationship(null,null,component.getName());
                    }
//                    workingDiagram.setupComponentForDiagram(newComponent); // Set drag listeners
                    component.setSelected(false); // panel grid component should not show as selected
                    workingDiagram.addComponent(newComponent);

                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
    }

    private void exportPanelAsPNG(JPanel panel) {
        for(int i = 0; i < workingDiagram.getComponentsCount(); i++)
        {
            workingDiagram.getListOfComponents().get(i).setSelected(false);
        }
        // Create a JFileChooser to allow the user to select the file location and name
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Diagram as PNG");
        fileChooser.setSelectedFile(new File("working_diagram.png"));  // Default file name

        // Show the save dialog and check if the user selected a file
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            // Get the file the user selected
            File fileToSave = fileChooser.getSelectedFile();

            // Make sure the file has a ".png" extension
            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.endsWith(".png")) {
                filePath += ".png";
            }

            // Create a BufferedImage to hold the panel content
            BufferedImage image = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_ARGB);

            // Paint the panel's content onto the BufferedImage
            Graphics2D g2d = image.createGraphics();
            panel.paint(g2d);  // Use paint() to include all the contents of the panel
            g2d.dispose();

            try {
                // Write the BufferedImage to the selected PNG file
                ImageIO.write(image, "png", new File(filePath));
                JOptionPane.showMessageDialog(this, "Diagram exported successfully to " + filePath);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to export diagram.");
            }
        }
    }
    private void exportPanelAsJPEG(JPanel panel) {
        for(int i = 0; i < workingDiagram.getComponentsCount(); i++)
        {
            workingDiagram.getListOfComponents().get(i).setSelected(false);
        }
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Diagram as JPEG");
        fileChooser.setSelectedFile(new File("working_diagram.jpeg")); // Default file name

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.endsWith(".jpeg") && !filePath.endsWith(".jpg")) {
                filePath += ".jpeg";
            }

            BufferedImage image = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = image.createGraphics();
            panel.paint(g2d);
            g2d.dispose();

            try {
                ImageIO.write(image, "jpeg", new File(filePath));
                JOptionPane.showMessageDialog(this, "Diagram exported successfully to " + filePath);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to export diagram.");
            }
        }
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
    public void chooseDirectory(ActionEvent e){
        JFileChooser directoryChooser = new JFileChooser();
        directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        directoryChooser.setDialogTitle("Select Directory to Generate Code Files");

        int userSelection = directoryChooser.showOpenDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File selectedDirectory = directoryChooser.getSelectedFile();
            System.out.println("Selected Directory: " + selectedDirectory.getAbsolutePath());

            CodeGenerator codeGenerator = new CodeGenerator(workingDiagram.components);
            try {
                codeGenerator.generateCode(selectedDirectory);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Could not generate code files: " + ex.getMessage(),
                        "Code Generation Error", JOptionPane.WARNING_MESSAGE
                );
            }

        } else {
            System.out.println("Directory selection was cancelled.");
        }
    }
}
