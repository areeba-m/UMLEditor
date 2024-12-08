package TestCases;

import BusinessLayer.Components.ClassDiagramComponents.ClassBox;
import BusinessLayer.Components.ClassDiagramComponents.ClassDiagramRelationship;
import BusinessLayer.Components.UMLComponent;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClassBoxTest {
    private ClassBox classBox;

    @BeforeEach
    public void setUp() {
        classBox = new ClassBox();
        classBox.setName("TestClass");
        classBox.setType("Regular");
        classBox.setAttributes((ArrayList<String>) new ArrayList<>(List.of("attribute1", "attribute2")));
        classBox.setMethods((ArrayList<String>) new ArrayList<>(List.of("method1()", "method2()")));
        classBox.setPoint(new Point(10, 20));
    }

    @Test
    public void testAddAttribute() {
        classBox.addAttribute(new String[] { "attribute3" });
        assertTrue(classBox.getAttributes().contains("attribute3"));
    }

    @Test
    public void testAddMethods() {
        classBox.addMethods(new String[] { "method3()" });
        assertTrue(classBox.getMethods().contains("method3()"));
    }

    @Test
    public void testToJSON() {
        JSONObject json = classBox.toJSON();

        assertEquals("TestClass", json.getString("name"));
        assertEquals("Regular", json.getString("classType"));
        assertEquals(2, json.getJSONArray("attributes").length());
        assertEquals(2, json.getJSONArray("methods").length());
        assertEquals(0, json.getJSONObject("point").getInt("x"));
        assertEquals(0, json.getJSONObject("point").getInt("y"));
    }

    @Test
    public void testAddRelationship() {
        UMLComponent fromComponent = new ClassBox(); // Mock or create a simple UMLComponent
        UMLComponent toComponent = new ClassBox(); // Mock or create another simple UMLComponent
        String relationshipName = "relationship1";
        ClassDiagramRelationship relationship = new ClassDiagramRelationship(fromComponent, toComponent, relationshipName);

        classBox.addRelationship(relationship);
        assertTrue(classBox.getRelationships().contains(relationship));
    }

    @Test
    public void testRemoveRelationship() {
        UMLComponent fromComponent = new ClassBox(); // Mock or create a simple UMLComponent
        UMLComponent toComponent = new ClassBox(); // Mock or create another simple UMLComponent
        String relationshipName = "relationship1";
        ClassDiagramRelationship relationship = new ClassDiagramRelationship(fromComponent, toComponent, relationshipName);

        classBox.addRelationship(relationship);
        classBox.removeRelationship(relationship);
        assertFalse(classBox.getRelationships().contains(relationship));
    }

    @Test
    public void testSetAttributes() {
        ArrayList<String> newAttributes = new ArrayList<>(List.of("attribute4", "attribute5"));
        classBox.setAttributes(newAttributes);
        assertEquals(newAttributes, classBox.getAttributes());
    }

    @Test
    public void testSetMethods() {
        ArrayList<String> newMethods = new ArrayList<>(List.of("method4()", "method5()"));
        classBox.setMethods(newMethods);
        assertEquals(newMethods, classBox.getMethods());
    }

    @Test
    public void testSetPoint() {
        Point newPoint = new Point(0, 0);
        classBox.setPoint(newPoint);
        assertEquals(newPoint, classBox.getPoint());
    }
}
