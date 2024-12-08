package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class SwitchButton extends JComponent {
    private String toolTipText;
    private boolean isOn = false; // Toggle state
    private final Color onColor = Color.DARK_GRAY;
    private final Color offColor = Color.LIGHT_GRAY;

    public SwitchButton() {
        setPreferredSize(new Dimension(50, 22));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                isOn = !isOn; // Toggle state
                repaint();    // Redraw the component
                fireActionEvent(); // Notify listeners
            }
        });
    }

    public boolean isOn() {
        return isOn;
    }

    private void fireActionEvent() {
        for (ActionListener listener : listenerList.getListeners(ActionListener.class)) {
            listener.actionPerformed(new java.awt.event.ActionEvent(this, java.awt.event.ActionEvent.ACTION_PERFORMED, null));
        }
    }

    public void addActionListener(ActionListener listener) {
        listenerList.add(ActionListener.class, listener);
    }

    public void removeActionListener(ActionListener listener) {
        listenerList.remove(ActionListener.class, listener);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw the background (rounded rectangle)
        g2.setColor(isOn ? onColor : offColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());

        // Draw the toggle circle
        int circleDiameter = getHeight() - 4;
        int circleX = isOn ? getWidth() - circleDiameter - 2 : 2;
        g2.setColor(Color.WHITE);
        g2.fillOval(circleX, 2, circleDiameter, circleDiameter);
    }

    @Override
    public JToolTip createToolTip() {
        CustomToolTip customToolTip = new CustomToolTip();
        customToolTip.setComponent(this); // Associate tooltip with this component
        return customToolTip;
    }

    @Override
    public String getToolTipText(MouseEvent event) {
        return toolTipText;
    }

    public void setCustomToolTipText(String text) {
        this.toolTipText = text;
        setToolTipText(text);
    }

    public String getState(){
        return this.isOn() ? "ON" : "OFF";
    }

    /*public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Custom Switch Button");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(300, 200);
            frame.setLayout(new FlowLayout());

            SwitchButton switchButton = new SwitchButton();
            switchButton.addActionListener(e -> {
                System.out.println("Switch State: " + (switchButton.isOn() ? "ON" : "OFF"));
            });

            frame.add(switchButton);
            frame.setVisible(true);
        });
    }*/
}
