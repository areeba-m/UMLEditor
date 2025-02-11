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

/**
 * Represents a Use Case Diagram in the UML editor.
 * Extends {@link UMLDiagram} and provides functionality specific to Use Case diagrams.
 *
 * <p>This class supports adding, removing, rendering components, and managing connections
 * between components like actors, use cases, and relationships.</p>
 */
@JsonIgnoreProperties({"accessibleContext", "graphicsConfiguration", "rootPane", "layeredPane", "contentPane", "transferHandler", "inputMap", "actionMap", "clientProperty", "focusTraversalPolicyProvider", "focusCycleRoot"}) // Ignore JPanel's internal properties
public class UseCaseDiagram extends UMLDiagram {

    /**
     * Constructs a new UseCaseDiagram with an empty list of components.
     */
    public UseCaseDiagram(){
        super();
        components = new ArrayList<>();

    }

    /**
     * Returns the list of components in the diagram.
     *
     * @return an {@link ArrayList} of {@link UMLComponent}.
     */
    @Override
    public ArrayList<UMLComponent> getListOfComponents() {
        return components;
    }

    /**
     * Returns the number of components in the diagram.
     *
     * @return the component count as an integer.
     */
    @Override
    public int getComponentsCount() {
        return components.size();
    }
    /**
     * Adds a new component to the diagram.
     *
     * @param component the {@link UMLComponent} to add.
     */
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

    /**
     * Removes a component from the diagram.
     *
     * @param component the {@link UMLComponent} to remove.
     */
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

    /**
     * Creates a connection between two components in the diagram.
     *
     * @param comp1 the first {@link UMLComponent}.
     * @param comp2 the second {@link UMLComponent}.
     * @param type  the type of connection ("Association", "Include", "Extend").
     * @return an integer indicating the connection status:
     *         <ul>
     *         <li>0: Success</li>
     *         <li>1: Cannot associate two use cases</li>
     *         <li>2: Cannot include an actor</li>
     *         <li>3: Cannot extend an actor</li>
     *         <li>4: Invalid component</li>
     *         <li>5: Self-connection not allowed</li>
     *         </ul>
     */
    @Override
    public int createConnection(UMLComponent comp1, UMLComponent comp2, String type) {

        if(comp1 instanceof UseCaseDiagramRelationship || comp2 instanceof UseCaseDiagramRelationship){
            return 4; // 4: invalid component
        }

        if(comp1 == comp2){
            return 5; // 5: connection between same comps not possible
        }

        if(type.equalsIgnoreCase("Association")) {

            if(comp1 instanceof UseCase && comp2 instanceof UseCase){
                return 1; // 1: cant associate 2 use cases
            }

            UseCaseDiagramRelationship relationship = new UseCaseDiagramRelationship(comp1, comp2, "Association");
            addComponent(relationship);

        } else if(type.equalsIgnoreCase("Include")) {
            if(comp1 instanceof Actor || comp2 instanceof Actor){
                return 2; // 2: cant include for actor
            }

            UseCaseDiagramRelationship relationship = new UseCaseDiagramRelationship(comp1, comp2, "Include");
            addComponent(relationship);

        } else if(type.equalsIgnoreCase("Extend")) {
            if(comp1 instanceof Actor || comp2 instanceof Actor){
                return 3; // 3: cant extend for actor
            }

            UseCaseDiagramRelationship relationship = new UseCaseDiagramRelationship(comp1, comp2, "Extend");
            addComponent(relationship);
        }

        revalidate();
        repaint();

        return 0; // normal
    }

    /**
     * Adds multiple components to the diagram.
     *
     * @param component the {@link UMLComponent} to add.
     */
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

    /**
     * Converts the diagram to a JSON representation.
     *
     * @return a {@link JSONObject} representing the Use Case diagram.
     */
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

    /**
     * Saves the diagram to a file in JSON format.
     *
     * @param filePath the file path to save the diagram.
     */
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


    /**
     * Loads a Use Case diagram from a JSON file.
     *
     * @param filePath the file path of the JSON file.
     * @return a {@link UMLDiagram} object representing the loaded diagram, or {@code null} on failure.
     */
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

