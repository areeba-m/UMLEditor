package BusinessLayer.Components;

import BusinessLayer.Components.ClassDiagramComponents.ClassBox;
import BusinessLayer.Diagrams.ClassDiagram;
import BusinessLayer.Diagrams.UMLDiagram;
import ui.UMLEditorForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class UMLComponent extends JComponent {
    protected String name;
    protected String classType;//i made this for class diagram

    protected Point point;

    protected boolean isGridPanel = false;
    protected boolean isDropped = false;

    protected Point dragOffset;
    private boolean isSelected = false;

    protected JTextArea textArea;

    public UMLComponent() {

        // Add mouse listeners for drag functionality
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Store the offset where the mouse was clicked
                dragOffset = e.getPoint();
                for(int i = 0; i < UMLDiagram.getComponentList().size(); i++)
                {
                    if(UMLDiagram.getComponentList().get(i).getLocation() == UMLComponent.this.getLocation())
                    {
                        UMLComponent.this.setSelected(true);
                        UMLDiagram.getComponentList().get(i).setSelected(true);
                    }
                    else {
                        UMLDiagram.getComponentList().get(i).setSelected(false);
                    }
                }
                 if(UMLComponent.this instanceof ClassBox) {
                    ClassBox obj = (ClassBox) UMLComponent.this;
                    obj.handleClassBoxMousePressed(e);
                 }
            }
            @Override
            public void mouseReleased(MouseEvent e){
                if(UMLComponent.this instanceof ClassBox){
                    ClassBox obj = (ClassBox) UMLComponent.this;
                    obj.handleClassBoxMouseReleased(e);
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // Update the component's location based on mouse movement
                if(UMLComponent.this instanceof ClassBox) {
                    ClassBox obj = (ClassBox) UMLComponent.this;
                    obj.handleClassBoxMouseDragged(e);
                }
//                Point newLocation = getParent().getMousePosition();
//                if (newLocation != null) {
//                    setLocation(newLocation.x - dragOffset.x, newLocation.y - dragOffset.y);
//                    getParent().revalidate();
//                    getParent().repaint();
//                }
            }
        });
    }

    public String getTextArea()
    {
        return textArea.getText();
    }
    public void SetTextArea(String text)
    {
        this.textArea.setText(text);
    }
    public String getType()
    {
        return classType;
    }

    public void setType(String type)
    {
        classType = type;
    }

    public void setIsDropped(boolean flag)
    {
        this.isDropped = flag;
    }

    public boolean getIsDropped()
    {
        return isDropped;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
        repaint();
    }

    public void setTextArea(JTextArea txtArea)
    {
        this.textArea = txtArea;
    }
    public void setIsGridPanel(boolean flag)
    {
        this.isGridPanel = flag;
    }

    public boolean getIsGridPanel()
    {
        return isGridPanel;
    }

    public void setName(String name)
    {
        this.name = name;
    }
    public String getName()
    {
        return name;
    }
    public abstract void updateFromTextArea();

    public abstract void draw(Graphics g);

}