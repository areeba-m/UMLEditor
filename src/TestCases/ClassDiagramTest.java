package TestCases;

import BusinessLayer.Components.ClassDiagramComponents.ClassBox;
import BusinessLayer.Components.ClassDiagramComponents.ClassDiagramRelationship;
import BusinessLayer.Diagrams.ClassDiagram;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class ClassDiagramTest {

    private ClassDiagram classDiagram;
    private ClassBox classBox1;
    private ClassBox classBox2;

    @BeforeEach
    void setUp() {
        classDiagram = new ClassDiagram();
        classBox1 = new ClassBox();
        classBox2 = new ClassBox();
    }

    @Test
    void testAddComponent() {
        // Test adding a single ClassBox component
        classDiagram.addComponent(classBox1);

        // Check if the component is added
        assertTrue(classDiagram.getListOfComponents().contains(classBox1));
        assertEquals(1, classDiagram.getComponentsCount());
    }

    @Test
    void testAddComponents() {
        // Test adding multiple ClassBox components
        classDiagram.addComponents(classBox1);
        classDiagram.addComponents(classBox2);

        // Check if both components are added
        assertTrue(classDiagram.getListOfComponents().contains(classBox1));
        assertTrue(classDiagram.getListOfComponents().contains(classBox2));
        assertEquals(2, classDiagram.getComponentsCount());
    }

    @Test
    void testRemoveComponent() {
        // Test removing a component
        classDiagram.addComponent(classBox1);
        classDiagram.removeComponent(classBox1);

        // Check if the component is removed
        assertFalse(classDiagram.getListOfComponents().contains(classBox1));
        assertEquals(0, classDiagram.getComponentsCount());
    }

@Test
void testToJson() {
    // Add components to the Class diagram
    ClassBox classBox1 = new ClassBox();
    classBox1.setType("ClassBox");
    classBox1.setName("Class");
    String[] list = {"-age:int"};
    classBox1.addAttribute(list);
    String[] list2 = {"-getAge():int"};
    classBox1.addMethods(list2);
    classBox1.setHeight(100);
    classBox1.setWidth(200);
    classBox1.setLocation(new Point(50, 50));

    classDiagram.addComponent(classBox1);

    // Generate the JSON object
    JSONObject json = classDiagram.toJson();

    // Check if the JSON object is created correctly
    assertNotNull(json, "JSON object should not be null");

    // Check the number of components in the JSON array
    assertEquals(1, json.getJSONArray("components").length(), "There should be 2 components in the JSON array");

    // Check specific attributes of components in the JSON array
    JSONArray componentsArray = json.getJSONArray("components");

    // Verify the attributes of the first component (ClassBox1)
    JSONObject component1 = componentsArray.getJSONObject(0);
    assertEquals("Class", component1.getString("name"), "First component name should be 'Class1'");
    assertEquals("ClassBox", component1.getString("type"), "First component type should be 'ClassBox'");
    assertTrue(component1.has("attributes"), "First component should have attributes");
    assertTrue(component1.has("methods"), "First component should have methods");
    assertEquals(100, component1.getInt("height"), "First component height should be 100");
    assertEquals(200, component1.getInt("width"), "First component width should be 200");
    assertEquals(50, component1.getJSONObject("location").getInt("x"), "First component location x should be 50");
    assertEquals(50, component1.getJSONObject("location").getInt("y"), "First component location y should be 50");

    // Check bounds for both components
    assertEquals(component1.getJSONObject("bounds").getInt("x"), 50, "First component bounds x should be 0");
    assertEquals(component1.getJSONObject("bounds").getInt("y"), 50, "First component bounds y should be 0");
    assertEquals(component1.getJSONObject("bounds").getInt("width"), 132, "First component bounds width should be 200");
    assertEquals(component1.getJSONObject("bounds").getInt("height"), 120, "First component bounds height should be 100");
}

    @Test
    void testSaveToFile() {
            // Add components to the Class diagram
            ClassBox classBox1 = new ClassBox();
            ClassBox classBox2 = new ClassBox();
            ClassDiagramRelationship relationship1 = new ClassDiagramRelationship(classBox1, classBox2, "Association");

            classDiagram.addComponent(classBox1);
            classDiagram.addComponent(classBox2);
            classDiagram.addComponent(relationship1);

            // Define file path to save the Class diagram
            String filePath = "test_class_diagram.json";
            classDiagram.saveToFile(filePath);

            // Check if the file was created
            File file = new File(filePath);
            assertTrue(file.exists(), "File should be created");
    }
    @Test
    void testLoadFromFile() {
        String filePath = "test_class_diagram.json";

        // Simulate saving to file
        ClassBox classBox1 = new ClassBox();
        ClassBox classBox2 = new ClassBox();
        ClassDiagramRelationship relationship1 = new ClassDiagramRelationship(classBox1, classBox2, "Association");
        classDiagram.addComponent(classBox1);
        classDiagram.addComponent(classBox2);
        classDiagram.addComponent(relationship1);
        classDiagram.saveToFile(filePath);

        // Load from file
        ClassDiagram loadedDiagram = (ClassDiagram) classDiagram.loadFromFile(filePath);

        assertNotNull(loadedDiagram, "Loaded diagram should not be null");

        // Check if the list of components in the loaded diagram matches the original components
        assertEquals(classDiagram.getListOfComponents().size(), loadedDiagram.getListOfComponents().size(), "Component counts should match");

        classDiagram.getListOfComponents().forEach(component -> assertTrue(loadedDiagram.getListOfComponents().contains(component), "All components should be present in the loaded diagram"));
    }

}
