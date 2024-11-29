package BusinessLayer.Components.UseCaseDiagramComponents;

import BusinessLayer.Components.UMLComponent;

import java.awt.*;

public class UseCase extends UMLComponent {
    int width;
    int height;

    public UseCase(String name) {
        super();
        this.name = name;
        setPreferredSize(new Dimension(200,200));

        this.point = new Point(0,0);
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        width = getWidth()/2;
        height = getHeight()/8;

        g2.setColor(Color.white);
        g2.fillOval(0,0, width, height);
        g2.setColor(Color.black);
        g2.drawOval(0,0, width, height);

        FontMetrics metrics = g2.getFontMetrics(getFont());
        int x = (width  - metrics.stringWidth(name)) / 2;
        int y = ((height - metrics.getHeight()) / 2) + metrics.getAscent();
        g2.drawString(name, x, y);
    }

    @Override
    public void draw(Graphics g) {

    }
}