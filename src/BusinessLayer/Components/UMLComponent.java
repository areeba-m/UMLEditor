package BusinessLayer.Components;

import BusinessLayer.Components.ClassDiagramComponents.ClassBox;
import BusinessLayer.Components.ClassDiagramComponents.ClassDiagramRelationship;
import BusinessLayer.Components.UseCaseDiagramComponents.UseCaseDiagramRelationship;
import BusinessLayer.Diagrams.ClassDiagram;
import BusinessLayer.Diagrams.UMLDiagram;
import org.json.JSONObject;
import ui.UMLEditorForm;
import BusinessLayer.Components.ClassDiagramComponents.ClassBox;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.List;

import static BusinessLayer.Diagrams.UMLDiagram.components;

/**
 * Abstract class UML Component extend JComponent for all components to be drawn
 *
 */
@JsonIgnoreProperties({
        "accessibleContext",
        "graphicsConfiguration",
        "rootPane",
        "layeredPane",
        "contentPane",
        "transferHandler",
        "inputMap",
        "actionMap",
        "clientProperty",
        "focusTraversalPolicyProvider",
        "focusCycleRoot",
        "UI", // Another internal property that could be ignored
        "componentOrientation", // Component orientation
        "focusTraversalPolicy", // Focus policy
        "focusOwner" // Current focus owner
})
public abstract class UMLComponent extends JComponent {
    protected String name;
    protected Point point;
    protected String classType;//i made this for class diagram

    protected Point dragOffset;
    private boolean isSelected = false;
    protected boolean isGridPanel = false;
    protected boolean isDropped = false;
    @JsonIgnore
    protected static JTextPane textArea;

    public UMLComponent() {

        textArea = UMLEditorForm.getTextArea();
        // Add mouse listeners for drag functionality
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dragOffset = e.getPoint();

                for (UMLComponent component :components) {
                    component.setSelected(false);
                }
                if (UMLComponent.this.contains(e.getPoint())) {
                    UMLComponent.this.setSelected(true);

                }

                if(UMLComponent.this instanceof ClassBox obj) {
                    obj.handleClassBoxMousePressed(e);
                }
                else if(UMLComponent.this instanceof ClassDiagramRelationship obj)
                {
                    obj.handleMousePressed();
                }
                else{
                    // class box specifically sets its own text
                    textArea.setText(getName());
                    textArea.requestFocus();
                }

            }
            @Override
            public void mouseReleased(MouseEvent e){
                boolean clickedOnComponent = false;
                // Check if the click is on any component
                for (UMLComponent component : components) {
                    if (component.contains(e.getPoint())) {
                        clickedOnComponent = true;
                        break;
                    }
                }
                // If no component was clicked, deselect all
                if (!clickedOnComponent) {
                    for (UMLComponent component : components) {
                        component.setSelected(false);
                    }
                }

                if(UMLComponent.this instanceof ClassBox obj){
                    //obj.handleClassBoxMouseReleased(e);
                }

            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {

                if(UMLComponent.this instanceof UseCaseDiagramRelationship ||
                        UMLComponent.this instanceof ClassDiagramRelationship){
                    return;
                }

                if(UMLComponent.this instanceof ClassBox obj) {
                    obj.handleClassBoxMouseDragged(e);
                }

                Point newLocation = getParent().getMousePosition();
                if (newLocation != null) {
                    setLocation(newLocation.x - dragOffset.x, newLocation.y - dragOffset.y);
                    getParent().revalidate();
                    getParent().repaint();
                }
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

        if (classType.equalsIgnoreCase("simple")){
            setPreferredSize(new Dimension(120, 100));
        } else if (classType.equalsIgnoreCase("abstract")){
            setPreferredSize(new Dimension(120, 100));
        } else if (classType.equalsIgnoreCase("interface")){
            setPreferredSize(new Dimension(120, 150));
        }
        revalidate();
        repaint();
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

    public void setTextArea(JTextPane txtArea)
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

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
        repaint();
    }
    public abstract void updateFromTextArea();

    public abstract void draw(Graphics g);
    public abstract void setMethods(List<Object> methods);

    public abstract void setAttributes(List<Object> attributes);

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }
    public void setHeight(int height) {
    }

    public void setWidth(int width) {

    }

    public void addAttribute(String[] att) {
    }

    public void addMethods(String[] meth) {

    }

    public abstract JSONObject toJSON();
}