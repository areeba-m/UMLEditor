package ui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConnectionDialog {

    public static void createRoundedDialog() {
        setUIFont(new Font("Segoe UI Emoji", Font.BOLD, 14));

        Color customColor = new Color(240, 240, 240);//new Color(105, 198, 194);
        // Create the dialog
        JDialog dialog = new JDialog();
        dialog.setTitle("Choose Connection Type");
        dialog.setSize(300, 200);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        dialog.setLocationRelativeTo(null);

        dialog.setResizable(false);

        dialog.setUndecorated(true); // Remove window frame (borderless)
        dialog.getRootPane().setWindowDecorationStyle(JRootPane.NONE);

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
        JLabel label = new JLabel("Choose a connection type");
        labelPanel.add(Box.createHorizontalGlue());
        labelPanel.add(label);
        labelPanel.add(Box.createHorizontalGlue());

        topPanel.add(Box.createVerticalStrut(20));
        topPanel.add(labelPanel);
        topPanel.add(Box.createVerticalStrut(20));

        panel.add(topPanel, BorderLayout.NORTH);

        JPanel radioPanel = new JPanel();
        radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.Y_AXIS));
        radioPanel.setBackground(customColor);

        // Create radio buttons
        JRadioButton associationButton = new JRadioButton("Association");
        JRadioButton includeButton = new JRadioButton("Include");
        JRadioButton excludeButton = new JRadioButton("Exclude");

        associationButton.setBackground(customColor);
        includeButton.setBackground(customColor);
        excludeButton.setBackground(customColor);

        // Group the radio buttons
        ButtonGroup group = new ButtonGroup();
        group.add(associationButton);
        group.add(includeButton);
        group.add(excludeButton);

        radioPanel.add(Box.createVerticalStrut(20));
        radioPanel.add(associationButton);
        radioPanel.add(includeButton);
        radioPanel.add(excludeButton);
        radioPanel.add(Box.createVerticalGlue());

        panel.add(radioPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Center align buttons

        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Perform action for OK button
                String selectedOption = getSelectedOption(associationButton, includeButton, excludeButton);
                System.out.println("Selected Option: " + selectedOption);
                dialog.dispose(); // Close dialog
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose(); // Close dialog on Cancel
            }
        });

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        Border border = BorderFactory.createLineBorder(Color.DARK_GRAY, 2);
        panel.setBorder(border); // Apply the border to the panel

        dialog.setContentPane(panel);

        //dialog.setShape(new java.awt.geom.RoundRectangle2D.Double(0, 0, dialog.getWidth(), dialog.getHeight(), 30, 30));

        dialog.setVisible(true);

    }

    private static String getSelectedOption(JRadioButton associationButton, JRadioButton includeButton, JRadioButton excludeButton) {
        if (associationButton.isSelected()) {
            return "Association";
        } else if (includeButton.isSelected()) {
            return "Include";
        } else if (excludeButton.isSelected()) {
            return "Exclude";
        } else {
            return "No option selected";
        }
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
}