package BusinessLayer.Components.UseCaseDiagramComponents;

import BusinessLayer.Components.UMLComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UseCase extends UMLComponent {
    String name;

    public UseCase(String name) {
        this.name = name;
        setPreferredSize(new Dimension(200,200));

        setTransferHandler(new TransferHandler("name")); // "name" is a placeholder property

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                getTransferHandler().exportAsDrag(UseCase.this, e, TransferHandler.COPY);
            }
        });

    }

    @Override
    public void draw(Graphics g) {

    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int width = getWidth()/2;
        int height = getHeight()/8;

        g2.setColor(Color.white);
        g2.fillOval(0,0, width, height);
        g2.setColor(Color.black);
        g2.drawOval(0,0, width, height);

        FontMetrics metrics = g2.getFontMetrics(getFont());
        int x = (width  - metrics.stringWidth(name)) / 2;
        int y = ((height - metrics.getHeight()) / 2) + metrics.getAscent();
        g2.drawString(name, x, y);
    }
}