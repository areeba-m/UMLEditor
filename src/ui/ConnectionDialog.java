package ui;

import BusinessLayer.Components.ClassDiagramComponents.ClassDiagramRelationship;
import BusinessLayer.Components.UMLComponent;
import BusinessLayer.Components.UseCaseDiagramComponents.UseCaseDiagramRelationship;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * Custom JDialog containing information about the class to make relationship with
 *
 */
public class ConnectionDialog extends JDialog{

    //JDialog dialog;
    private JList<String> componentList;

    private Map<String, UMLComponent> nameToComponentMap;
    private String selectedComponentName;

    String currentComponentName;
    UMLComponent selectedComponent;
    ArrayList<UMLComponent> components;

    public ConnectionDialog(JFrame parent, String name, ArrayList<UMLComponent> components) {
        super(parent, "Select Connection Type", true);

        this.components = components;
        this.currentComponentName = name;
        setTitle("Choose Connection Type");
        setSize(300, 300);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true); // Remove window frame (borderless)
        getRootPane().setWindowDecorationStyle(JRootPane.NONE);

        createDialog();

        setUIFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));

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

    public void createDialog(){
        Color customColor = new Color(240, 240, 240);//new Color(105, 198, 194);

        // Create content panel
        JPanel panel = new JPanel();
        panel.setBackground(customColor);
        panel.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        topPanel.setBackground(Color.gray);

        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.X_AXIS));
        labelPanel.setBackground(Color.gray);
        JLabel label = new JLabel("Choose a component");
        labelPanel.add(Box.createHorizontalGlue());
        labelPanel.add(label);
        labelPanel.add(Box.createHorizontalGlue());

        topPanel.add(Box.createVerticalStrut(20));
        topPanel.add(labelPanel);
        topPanel.add(Box.createVerticalStrut(20));

        panel.add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel,BoxLayout.Y_AXIS));
        centerPanel.setBackground(customColor);

        JPanel fromPanel = new JPanel();
        fromPanel.setLayout(new BoxLayout(fromPanel,BoxLayout.X_AXIS));
        fromPanel.setBackground(customColor);

        JLabel currentComponent = new JLabel(currentComponentName);
        JLabel from = new JLabel("From");
        from.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        JLabel to = new JLabel("To");
        to.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));

        fromPanel.add(from);
        fromPanel.add(Box.createHorizontalStrut(20));
        fromPanel.add(currentComponent);
        fromPanel.add(Box.createHorizontalGlue());
        centerPanel.add(fromPanel);

        JPanel toPanel = new JPanel();
        toPanel.setLayout(new BoxLayout(toPanel,BoxLayout.X_AXIS));
        toPanel.setBackground(customColor);

        toPanel.add(to);
        toPanel.add(Box.createHorizontalGlue());
        centerPanel.add(toPanel);

        nameToComponentMap = new HashMap<>();
        DefaultListModel<String> listModel = new DefaultListModel<>();

        // Populate the list model and the map
        int counter = 1;
        for (UMLComponent component : components) {
            if(!(component instanceof ClassDiagramRelationship || component instanceof UseCaseDiagramRelationship)) {
                String name = (component.getName() != null) ? component.getName() : "Component " + counter++;
                listModel.addElement(name);
                nameToComponentMap.put(name, component);
            }
        }

        componentList = new JList<>(listModel);
        componentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(componentList);

        centerPanel.add(scrollPane);
        panel.add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Center align buttons

        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                selectedComponentName = componentList.getSelectedValue();
                selectedComponent = nameToComponentMap.get(selectedComponentName); // set selected comp

                System.out.println("Selected Component: " + selectedComponentName);
                dispose();

            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedComponent = null;
                dispose(); // Close dialog on Cancel
            }
        });

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        Border border = BorderFactory.createLineBorder(Color.DARK_GRAY, 2);
        panel.setBorder(border); // Apply the border to the panel

        setContentPane(panel);

        //dialog.setShape(new java.awt.geom.RoundRectangle2D.Double(0, 0, dialog.getWidth(), dialog.getHeight(), 30, 30));

        setVisible(true);
    }

    public UMLComponent getSelectedComponent() {
        return selectedComponent;
    }
}