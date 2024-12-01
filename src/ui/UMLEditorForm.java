package ui;
import BusinessLayer.Components.UMLComponent;
import BusinessLayer.Components.ClassDiagramComponents.ClassBox;
import BusinessLayer.Components.ClassDiagramComponents.ClassDiagramRelationship;
import BusinessLayer.Components.UseCaseDiagramComponents.Actor;
import BusinessLayer.Components.UseCaseDiagramComponents.UseCase;
import BusinessLayer.Components.UseCaseDiagramComponents.UseCaseDiagramRelationship;
import BusinessLayer.Diagrams.ClassDiagram;
import BusinessLayer.Diagrams.UMLDiagram;
import BusinessLayer.Diagrams.UseCaseDiagram;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;
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
    // ree comes in
    UMLDiagram workingDiagram;
    // ree goes

    JScrollPane canvasScrollPane;

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

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
                ArrayList<UMLComponent> components = workingDiagram.getComponentArr();

                for (UMLComponent component : components) {
                    if (!(component instanceof ClassBox || component instanceof ClassDiagramRelationship)
                            && component.isSelected()) {
                        component.setName(textArea.getText());
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

        switchButton.addActionListener(e -> {
            isConnectMode = switchButton.isOn();
            switchButton.setCustomToolTipText("Connect Mode: " + switchButton.getState());

        });

        JPanel switchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        switchPanel.setOpaque(false);
        switchPanel.add(switchButton);

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
                super.paintComponent(g); // Paint the default background (white)

                // Set the background to white
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, getWidth(), getHeight());

                // Draw the dotted background
                drawDottedBackground(g);
            }

            private void drawDottedBackground(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;

                // Set the dot color to light gray
                g2d.setColor(new Color(211, 211, 211)); // Light gray dots

                // Define dot size and spacing
                int dotSize = 2;       // Diameter of each dot (small-sized)
                int dotSpacing = 15;   // Space between dots

                // Draw dots at regular intervals
                for (int x = 0; x < getWidth(); x += dotSpacing) {
                    for (int y = 0; y < getHeight(); y += dotSpacing) {
                        g2d.fillOval(x, y, dotSize, dotSize);
                    }
                }
            }
        };
        panelGrid.setPreferredSize(new Dimension(500,500));
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
            int offsetX = 55;
            int offsetY = 50;

            // Create and position each ClassBox
            ClassBox simpleClass = new ClassBox();
            simpleClass.setName("SimpleClass");
            simpleClass.setType("simple");
            simpleClass.getPoint().setLocation(startX, startY);
            simpleClass.setIsGridPanel(true);
            simpleClass.setSelected(false);

            ClassBox concreteClass = new ClassBox();
            concreteClass.setName("ConcreteClass");
            concreteClass.setType("simple");
            concreteClass.getPoint().setLocation(startX, startY);
            concreteClass.setIsGridPanel(true);
            concreteClass.setSelected(false);

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

            simpleClass.setBounds(10, 10, 200, 200);
            abstractClass.setBounds(200, 10, 200, 200);
            interfaceClass.setBounds(10, 150, 200, 200);
            concreteClass.setBounds(200, 150, 200, 200);

            setupComponentForGrid(simpleClass);
            setupComponentForGrid(abstractClass);
            setupComponentForGrid(interfaceClass);
            setupComponentForGrid(concreteClass);

            panelGrid.add(simpleClass);
            panelGrid.add(abstractClass);
            panelGrid.add(interfaceClass);
            panelGrid.add(concreteClass);
            panelGrid.add(inheritance);
            panelGrid.add(association);
            panelGrid.add(aggregation);
            panelGrid.add(composition);

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
            UseCaseDiagramRelationship excludeRelationship = new UseCaseDiagramRelationship(uc1, uc2, "Exclude");
            UseCaseDiagramRelationship associationRelationship = new UseCaseDiagramRelationship(actor, uc1, "Association");

            includeRelationship.setManualBounds(new Point(125, 165), new Point(275, 365));
            excludeRelationship.setManualBounds(new Point(125, 145), new Point(375, 345));
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
            panelGrid.add(excludeRelationship);
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
                        //Point end = new Point((int) (e.getPoint().getX() + 30), (int) (e.getPoint().getY()+0));
                        //newComponent = new ClassDiagramRelationship(e.getPoint(), end);
                    } else if (component instanceof Actor) {
                        newComponent = new Actor(component.getName());
                    } else if (component instanceof UseCase) {
                        newComponent = new UseCase(component.getName());
                    } else if (component instanceof UseCaseDiagramRelationship) {
                       // newComponent = new UseCaseDiagramRelationship(null,null,component.getName());
                    }
                    //workingDiagram.setupComponentForDiagram(newComponent); // Set drag listeners
                    component.setSelected(false); // panel grid component should not show as selected
                    workingDiagram.addComponent(newComponent);

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
