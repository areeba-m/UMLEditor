package ui;

import BusinessLayer.Components.ClassDiagramComponents.ClassBox;
import BusinessLayer.Components.UseCaseDiagramComponents.Actor;
import BusinessLayer.Components.UseCaseDiagramComponents.UseCase;
import BusinessLayer.Diagrams.UseCaseDiagram;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    DrawingCanvas drawingCanvas;
    JScrollPane canvasScrollPane;

    // diagram components
    JPanel panelEast;
    JComboBox<String> cmbDiagramType;
    JPanel panelComboBox;
    JPanel panelGrid;
    JPanel panelTopEast;
    JScrollPane diagramTypeScrollPane;

    // text area components
    JTextArea textArea;
    JScrollPane textAreaScrollPane;
    JPanel panelBottomEast;

    public UMLEditorForm(){

        setTitle("UML Editor Application");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        setUIFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));

        prepareMenuBar();
        prepareCanvas();
        prepareDiagramType();

        loadGrid();

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

        menuBar.add(fileMenu);
        menuBar.add(exportMenu);
        menuBar.add(codeMenu);

        setJMenuBar(menuBar);
    }

    private void prepareCanvas(){
        drawingCanvas = new DrawingCanvas();
        canvasScrollPane = new JScrollPane(drawingCanvas);
        canvasScrollPane.setPreferredSize(new Dimension(800,600));

        add(canvasScrollPane, BorderLayout.CENTER);

    }

    private void prepareDiagramType(){
        String[] type = {"UML Class", "UML Use Case"};
        cmbDiagramType = new JComboBox<>(type);

        panelComboBox = new JPanel();
        panelComboBox.setLayout(new BoxLayout(panelComboBox, BoxLayout.Y_AXIS));
        panelComboBox.add(cmbDiagramType);

        panelGrid = new JPanel(new GridLayout(0,2));
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

        textAreaScrollPane = new JScrollPane(textArea);

        panelBottomEast = new JPanel();
        panelBottomEast.setPreferredSize(new Dimension(400,300));
        panelBottomEast.setLayout(new BoxLayout(panelBottomEast, BoxLayout.Y_AXIS));
        panelBottomEast.add(textAreaScrollPane);

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

        if ("UML Class".equals(diagramType)) {

            // Add more UML class components if needed
        } else if ("UML Use Case".equals(diagramType)) {
            panelGrid.add(new UseCase("Use Case"));
            panelGrid.add(new UseCase("Use Case 2"));
            panelGrid.add(new UseCase("Use Case 3"));

            // Add more UML use case components if needed
        }

        panelGrid.revalidate();
        panelGrid.repaint();
    }

    public static void main(String[] args){
        UMLEditorForm app = new UMLEditorForm();
        app.setVisible(true);
    }
}
