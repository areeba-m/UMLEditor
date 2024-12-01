package ui;

import javax.swing.*;
import java.awt.*;

public class CustomToolTip extends JToolTip {
    private JLabel label;

    public CustomToolTip() {
        super();
        setLayout(new BorderLayout());

        label = new JLabel("", SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(Color.white);
        label.setForeground(Color.BLACK);
        label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Padding around text

        add(label, BorderLayout.CENTER);

        setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }

    @Override
    public void setTipText(String tipText) {
        label.setText(tipText); // Update the label text
        revalidate();
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension size = label.getPreferredSize();
        size.width += 10; // Add padding
        return size;
    }
}
