package BusinessLayer.Diagrams;

import BusinessLayer.Components.ClassDiagramComponents.ClassBox;
import BusinessLayer.Components.UMLComponent;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.json.JSONObject;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

@JsonIgnoreProperties({"accessibleContext", "graphicsConfiguration", "rootPane", "layeredPane", "contentPane", "transferHandler", "inputMap", "actionMap", "clientProperty", "focusTraversalPolicyProvider", "focusCycleRoot"}) // Ignore JPanel's internal properties
public class ClassDiagram extends UMLDiagram {


    public ClassDiagram() {

        this.components = new ArrayList<>();
        this.name = "";
        setLayout(null);
        setPreferredSize(new Dimension(2000,2000));
        setBackground(Color.PINK);
    }

    @Override
    public ArrayList<UMLComponent> getListOfComponents() {
        return components;
    }

    public int getComponentsCount() {
        return components.size();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (UMLComponent component : components) {
            component.repaint();
        }
    }

    @Override
    public void addComponent(UMLComponent component) {
        System.out.println("Add called for component: " + component);

        components.add(component);
        setupComponentForDiagram(component);
        add(component);
        component.setBounds(50, 50, component.getPreferredSize().width, component.getPreferredSize().height);
        revalidate();
        repaint();
    }

    @Override
    public void removeComponent(UMLComponent component) {
        if (components.contains(component)) { // Remove only if present
            components.remove(component);
            remove(component);
            revalidate();
            repaint();
        }
    }


    @Override
    public void renderComponents(Graphics g) {

    }

    public JSONObject toJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Pretty print JSON
        // Create a custom object for ClassDiagram without JPanel properties
        JSONObject classDiagramJson = new JSONObject();
        classDiagramJson.put("name", this.getName());

        // Create a JSON array for the components
        for (UMLComponent component : getComponentList()) {
            if (component instanceof ClassBox) {
                ClassBox classBox = (ClassBox) component;
                // Add the JSON representation of ClassBox to the class diagram JSON object
                classDiagramJson.put("components", classBox.toJSON());  // Add ClassBox JSON
            }
        }

        return classDiagramJson;
    }

    public void saveToFile(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Enable pretty print

        // Convert the object to a pretty-printed JSON string
        JSONObject jsonObj = toJson();
        Map<String, Object> map = jsonObj.toMap();
        // If the JSON string is not null, write it to the file
        if (jsonObj != null) {
            try (FileWriter fileWriter = new FileWriter(new File(filePath))) {
                // Convert the object to JSON with pretty-printing and write to file
                objectMapper.writeValue(fileWriter, map); // Using objectMapper for pretty printing
                System.out.println("Project saved successfully to " + filePath);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to save the project.");
            }
        } else {
            System.out.println("Error: Unable to generate JSON.");
        }
    }


    // Load method
    public UMLDiagram loadFromFile(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        // Register the custom deserializer for ClassDiagram
        SimpleModule module = new SimpleModule();
        module.addDeserializer(ClassDiagram.class, new ClassDiagramDeserializer());
        objectMapper.registerModule(module);

        try {
            return objectMapper.readValue(new File(filePath), ClassDiagram.class);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load the project.");
            return null;
        }
    }
}