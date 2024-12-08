package BusinessLayer.Diagrams;

import BusinessLayer.Components.ClassDiagramComponents.ClassBox;
import BusinessLayer.Components.UMLComponent;
import BusinessLayer.Components.ClassDiagramComponents.ClassDiagramRelationship;
import BusinessLayer.Components.UseCaseDiagramComponents.UseCaseDiagramRelationship;
import ui.ConnectionDialog;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.*;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

@JsonIgnoreProperties({"accessibleContext", "graphicsConfiguration", "rootPane", "layeredPane", "contentPane", "transferHandler", "inputMap", "actionMap", "clientProperty", "focusTraversalPolicyProvider", "focusCycleRoot"}) // Ignore JPanel's internal properties
public class ClassDiagram extends UMLDiagram {

    public ClassDiagram() {
        super();
        this.components = new ArrayList<>();
        this.name = "";
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (UMLComponent component : components) {
            component.repaint();
        }
    }
    @Override
    public ArrayList<UMLComponent> getListOfComponents() {
        return components;
    }

    public int getComponentsCount() {
        return components.size();
    }

    @Override
    public void addComponent(UMLComponent component) {
        if(components.contains(component)){
            System.out.println("COMPONENT EXISTS: called Add component for " + component);
            return;
        }

        System.out.println("Add called for component: " + component);

        components.add(component);
        setupComponentForDiagram(component);
        add(component);
        component.setBounds(50, 50, component.getPreferredSize().width, component.getPreferredSize().height);
        revalidate();
        repaint();
    }

    public void addComponents(UMLComponent component) {
        if(components.contains(component)){
            System.out.println("COMPONENT EXISTS: called Add component for " + component);
            return;
        }

        System.out.println("Add called for component: " + component);

        components.add(component);
        setupComponentForDiagram(component);
        add(component);
        revalidate();
        repaint();
    }

    @Override
    public void removeComponent(UMLComponent component) {
        if (components.contains(component)) { // Remove only if present

            if(component instanceof ClassBox componentToRemove){ // safely deleting

                for(ClassDiagramRelationship relationship: componentToRemove.getRelationships()){

                    if(relationship.getTo() instanceof ClassBox otherClass && otherClass != componentToRemove){
                        // if 'to' is this class, don't delete relationship yet
                        otherClass.removeRelationship(relationship); // relationship was added to two classes, delete from other class first
                        components.remove(relationship); // remove from array
                        remove(relationship); // remove from diagram
                    }
                    else if(relationship.getFrom() instanceof ClassBox otherClass && otherClass != componentToRemove){
                        otherClass.removeRelationship(relationship);
                        components.remove(relationship);
                        remove(relationship);
                    } else {
                        // self-association
                        components.remove(relationship);
                        remove(relationship);
                    }
                }

            } else if(component instanceof  ClassDiagramRelationship obj){
                obj.removeFromAndTo();
            }

            components.remove(component);
            remove(component);
            revalidate();
            repaint();
        }
    }


    @Override
    public void renderComponents(Graphics g) {

    }
    @Override
    public int createConnection(UMLComponent comp1, UMLComponent comp2, String type) {

        if(comp1 instanceof ClassDiagramRelationship || comp2 instanceof ClassDiagramRelationship){
            return 4; // 4: invalid component
        }

        if(comp1 == comp2){
            return 5; // 5: connection between same comps not possible
        }

        if(type.equalsIgnoreCase("Association")) {
            ClassDiagramRelationship relationship = new ClassDiagramRelationship(comp1, comp2, "Association");
            addComponent(relationship);
        } else if(type.equalsIgnoreCase("aggregation")) {
            ClassDiagramRelationship relationship = new ClassDiagramRelationship(comp1, comp2, "aggregation");
            addComponent(relationship);
        } else if(type.equalsIgnoreCase("composition")) {
            ClassDiagramRelationship relationship = new ClassDiagramRelationship(comp1, comp2, "composition");
            addComponent(relationship);
        } else if(type.equalsIgnoreCase("inheritance")) {
            ClassDiagramRelationship relationship = new ClassDiagramRelationship(comp1, comp2, "inheritance");
            addComponent(relationship);
        }
        revalidate();
        repaint();
        return 0;
    }
    public JSONObject toJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Pretty print JSON
        // Create a custom object for ClassDiagram without JPanel properties
        JSONObject classDiagramJson = new JSONObject();
        classDiagramJson.put("name", this.getName());
        //creating json array to put multiple components in the components node
        JSONArray componentsArray = new JSONArray();
// Iterate through the list of UML components
        for (UMLComponent component : getComponentList()) {
            if (component instanceof ClassBox) {
                ClassBox classBox = (ClassBox) component;
                // Add the JSON representation of ClassBox to the components array
                componentsArray.put(classBox.toJSON());
            }
            else if (component instanceof ClassDiagramRelationship) {
                ClassDiagramRelationship relationship = (ClassDiagramRelationship) component;
                // Add the JSON representation of ClassBox to the components array
                componentsArray.put(relationship.toJSON());
            }
        }
// Add the JSON array to the class diagram JSON object
        classDiagramJson.put("components", componentsArray);

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