package BusinessLayer.Components;

import javax.swing.*;
import java.awt.*;

public abstract class UMLComponent extends JComponent {
    int x;
    int y;
    Point location;

    public abstract void draw(Graphics g);

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}