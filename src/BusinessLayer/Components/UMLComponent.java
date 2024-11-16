package BusinessLayer.Components;

import javax.swing.*;
import java.awt.*;

public abstract class UMLComponent extends JComponent {
//    int x;
//    int y;
protected Point point;

    public abstract void draw(Graphics g);

    public int getX() {

        return (int) point.getX();
    }

    public void setX(int x) {

        this.point.x = x;
    }

    public int getY() {

        return (int) point.getY();
    }

    public void setY(int y) {

        this.point.y = y;
    }
}