package BusinessLayer.Components;

import javax.swing.*;
import java.awt.*;

public abstract class UMLComponent extends JComponent {
    protected String name;
    protected Point point;

    public abstract void draw(Graphics g);

}