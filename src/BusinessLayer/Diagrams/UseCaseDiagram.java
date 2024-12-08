package BusinessLayer.Diagrams;

import BusinessLayer.Components.ClassDiagramComponents.ClassBox;
import BusinessLayer.Components.ClassDiagramComponents.ClassDiagramRelationship;
import BusinessLayer.Components.UMLComponent;
import BusinessLayer.Components.UseCaseDiagramComponents.Actor;
import BusinessLayer.Components.UseCaseDiagramComponents.UseCase;
import BusinessLayer.Components.UseCaseDiagramComponents.UseCaseComponent;
import BusinessLayer.Components.UseCaseDiagramComponents.UseCaseDiagramRelationship;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.json.JSONArray;
import org.json.JSONObject;
import ui.ConnectionDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
@JsonIgnoreProperties({"accessibleContext", "graphicsConfiguration", "rootPane", "layeredPane", "contentPane", "transferHandler", "inputMap", "actionMap", "clientProperty", "focusTraversalPolicyProvider", "focusCycleRoot"}) // Ignore JPanel's internal properties
public class UseCaseDiagram extends UMLDiagram {

    public UseCaseDiagram(){
        super();
        components = new ArrayList<>();

    }

    @Override
    public ArrayList<UMLComponent> getListOfComponents() {
        return components;
    }

    @Override
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

    @Override
    public void removeComponent(UMLComponent component) {
        components.remove(component);
        remove(component);
        revalidate();
        repaint();
    }

    @Override
    public void renderComponents(Graphics g) {

    }

    @Override
    protected void createConnection(UMLComponent comp1, UMLComponent comp2) {

        JFrame frame = new JFrame("Diagram");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(300, 300);
        frame.setLocationRelativeTo(null);

        ConnectionDialog dialog = new ConnectionDialog(frame, "UseCase");

        if(dialog.getOptionSelected().equalsIgnoreCase("Association")) {
            UseCaseDiagramRelationship relationship = new UseCaseDiagramRelationship(comp1, comp2, "Association");
            addComponent(relationship);
        } else if(dialog.getOptionSelected().equalsIgnoreCase("Include")) {
            if(comp1 instanceof Actor || comp2 instanceof Actor){
                //throw exception;
            }

            UseCaseDiagramRelationship relationship = new UseCaseDiagramRelationship(comp1, comp2, "Include");
            addComponent(relationship);
        } else if(dialog.getOptionSelected().equalsIgnoreCase("Extend")) {
            if(comp1 instanceof Actor || comp2 instanceof Actor){
                //throw exception;
            }

            UseCaseDiagramRelationship relationship = new UseCaseDiagramRelationship(comp1, comp2, "Extend");
            addComponent(relationship);
        }

        revalidate();
        repaint();
    }

    @Override
    public void addComponents(UMLComponent component) {
        if(components.contains(component)){
            System.out.println("COMPONENT EXISTS: called Add component for " + component);
            return;
        }

        System.out.println("Add called for component: " + component);
        components.add(component);
        setupComponentForDiagram(component);

        add(component);
        //component.setBounds(50, 50, component.getPreferredSize().width, component.getPreferredSize().height);
        revalidate();
        repaint();
    }

    public JSONObject toJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Pretty print JSON
        // Create a custom object for ClassDiagram without JPanel properties
        JSONObject useCaseDiagramJson = new JSONObject();
        useCaseDiagramJson.put("name", this.getName());
        //creating json array to put multiple components in the components node
        JSONArray componentsArray = new JSONArray();
        // Iterate through the list of UML components
        for (UMLComponent component : getComponentList()) {
            if (component instanceof Actor) {
                Actor actor = (Actor) component;
                // Add the JSON representation of ClassBox to the components array
                componentsArray.put(actor.toJSON());
            }
            else if(component instanceof UseCase)
            {
                UseCase usecase = (UseCase) component;
                componentsArray.put(usecase.toJSON());
            }
            else if (component instanceof UseCaseDiagramRelationship) {
                UseCaseDiagramRelationship relationship = (UseCaseDiagramRelationship) component;
                // Add the JSON representation of ClassBox to the components array
                componentsArray.put(relationship.toJSON());
            }
        }
        // Add the JSON array to the class diagram JSON object
        useCaseDiagramJson.put("components", componentsArray);

        return useCaseDiagramJson;
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
        module.addDeserializer(UseCaseDiagram.class, new UseCaseDiagramDeserializer());
        objectMapper.registerModule(module);

        try {
            return objectMapper.readValue(new File(filePath), UseCaseDiagram.class);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load the project.");
            return null;
        }
    }

}

