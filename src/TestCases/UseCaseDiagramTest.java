package TestCases;

import BusinessLayer.Components.UMLComponent;
import BusinessLayer.Diagrams.UMLDiagram;
import BusinessLayer.Diagrams.UseCaseDiagram;
import BusinessLayer.Components.ClassDiagramComponents.ClassBox;
import BusinessLayer.Components.ClassDiagramComponents.ClassDiagramRelationship;
import BusinessLayer.Components.UseCaseDiagramComponents.Actor;
import BusinessLayer.Components.UseCaseDiagramComponents.UseCase;
import BusinessLayer.Components.UseCaseDiagramComponents.UseCaseDiagramRelationship;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class UseCaseDiagramTest {

    UseCaseDiagram diagram;

    @BeforeEach
    void setUp() {
        diagram = new UseCaseDiagram();
    }

    @Test
    void testAddComponent_Actor() {
        Actor actor = new Actor("Actor1");
        diagram.addComponent(actor);

        assertTrue(diagram.getListOfComponents().contains(actor), "Actor should be added to the diagram");
    }

    @Test
    void testAddComponent_UseCase() {
        UseCase useCase = new UseCase("UseCase1");
        diagram.addComponent(useCase);

        assertTrue(diagram.getListOfComponents().contains(useCase), "UseCase should be added to the diagram");
    }

    @Test
    void testAddComponent_Relationship() {
        UseCaseDiagramRelationship relationship = new UseCaseDiagramRelationship(new Actor("Actor1"), new UseCase("UseCase1"), "Association");
        diagram.addComponent(relationship);

        assertTrue(diagram.getListOfComponents().contains(relationship), "Relationship should be added to the diagram");
    }

    @Test
    void testRemoveComponent() {
        Actor actor = new Actor("Actor1");
        diagram.addComponent(actor);
        diagram.removeComponent(actor);

        assertFalse(diagram.getListOfComponents().contains(actor), "Actor should be removed from the diagram");
    }

    @Test
    void testCreateConnection_ValidAssociation() {
        Actor actor = new Actor("Actor1");
        UseCase useCase = new UseCase("UseCase1");
        diagram.addComponent(actor);
        diagram.addComponent(useCase);

        diagram.createConnection(actor, useCase);

        boolean relationshipExists = diagram.getListOfComponents().stream()
                .anyMatch(component -> component instanceof UseCaseDiagramRelationship &&
                        ((UseCaseDiagramRelationship) component).getName().equals("Association"));

        assertTrue(relationshipExists, "Association connection should be created");
    }

    @Test
    void testSaveToFile() {
        UseCase useCase = new UseCase("UseCase1");
        Actor actor = new Actor("Actor1");
        diagram.addComponent(useCase);
        diagram.addComponent(actor);

        String filePath = "test_diagram.json";
        diagram.saveToFile(filePath);

        File file = new File(filePath);
        assertTrue(file.exists(), "File should be created");
    }

    @Test
    void testLoadFromFile() {
        String filePath = "test_diagram.json";

        // Simulate saving to file
        UseCase useCase = new UseCase("UseCase1");
        Actor actor = new Actor("Actor1");
        diagram.addComponent(useCase);
        diagram.addComponent(actor);
        diagram.saveToFile(filePath);

        // Load from file
        UseCaseDiagram loadedDiagram = (UseCaseDiagram) diagram.loadFromFile(filePath);

        assertNotNull(loadedDiagram, "Loaded diagram should not be null");

        // Check if the list of components in the loaded diagram matches the original components
        assertEquals(diagram.getListOfComponents().size(), loadedDiagram.getListOfComponents().size(), "Component counts should match");

        for (UMLComponent component : diagram.getListOfComponents()) {
            assertTrue(loadedDiagram.getListOfComponents().contains(component), "All components should be present in the loaded diagram");
        }
    }


    @Test
    void testLoadFromFile_InvalidPath() {
        assertNull(diagram.loadFromFile("invalid_path.json"), "Loading from an invalid path should return null");
    }

    @Test
    void testToJson() {
        UseCase useCase = new UseCase("UseCase1");
        Actor actor = new Actor("Actor1");
        diagram.addComponent(useCase);
        diagram.addComponent(actor);

        String jsonString = diagram.toJson().toString();

        assertTrue(jsonString.contains("UseCase1"), "JSON should contain UseCase details");
        assertTrue(jsonString.contains("Actor1"), "JSON should contain Actor details");
    }
}
