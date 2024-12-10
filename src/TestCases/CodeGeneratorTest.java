package TestCases;

import BusinessLayer.Components.ClassDiagramComponents.ClassBox;
import BusinessLayer.Components.ClassDiagramComponents.ClassDiagramRelationship;
import Data.CodeGenerator;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Hashtable;

import static org.junit.jupiter.api.Assertions.*;

class CodeGeneratorTest {

    private final CodeGenerator codeGenerator = new CodeGenerator();

    @Test
    void generateCode() {
    }

    @Test
    void generateClassAttributes_CorrectInput() {
        String att1 = "_#id:int _"; // becomes: protected static int id
        String att2 = "- name : String"; // becomes: private String name
        String att3 = " age: double"; // becomes: private double age

        ArrayList<String> attributes = new ArrayList<>();
        attributes.add(att1);
        attributes.add(att2);
        attributes.add(att3);

        ArrayList<Hashtable<String, String>> expectedAttributes = new ArrayList<>();
        Hashtable<String, String> attribute1 = new Hashtable<>();
        attribute1.put("att_access_modifier", "protected");
        attribute1.put("att_static", "static");
        attribute1.put("att_datatype", "int");
        attribute1.put("att_identifier", "id");
        expectedAttributes.add(attribute1);

        Hashtable<String, String> attribute2 = new Hashtable<>();
        attribute2.put("att_access_modifier", "private");
        attribute2.put("att_static", "");
        attribute2.put("att_datatype", "String");
        attribute2.put("att_identifier", "name");
        expectedAttributes.add(attribute2);

        Hashtable<String, String> attribute3 = new Hashtable<>();
        attribute3.put("att_access_modifier", "private");
        attribute3.put("att_static", "");
        attribute3.put("att_datatype", "double");
        attribute3.put("att_identifier", "age");
        expectedAttributes.add(attribute3);

        ArrayList<Hashtable<String, String>> actualAttributes = codeGenerator.generateClassAttributes(attributes);

        assertEquals(expectedAttributes, actualAttributes, "Should extract all attributes");

    }

    @Test
    void generateClassAttributes_IncorrectInput() {
        String att1 = "_#id:int "; // becomes: protected int id
        String att2 = "- name :"; // is skipped
        String att3 = "_ : double"; // is skipped

        ArrayList<String> attributes = new ArrayList<>();
        attributes.add(att1);
        attributes.add(att2);
        attributes.add(att3);

        ArrayList<Hashtable<String, String>> expectedAttributes = new ArrayList<>();
        Hashtable<String, String> attribute1 = new Hashtable<>();
        attribute1.put("att_access_modifier", "protected");
        attribute1.put("att_static", "");
        attribute1.put("att_datatype", "int");
        attribute1.put("att_identifier", "id");
        expectedAttributes.add(attribute1);

        ArrayList<Hashtable<String, String>> actualAttributes = codeGenerator.generateClassAttributes(attributes);

        assertEquals(expectedAttributes, actualAttributes, "Should extract attributes one attribute");

    }

    @Test
    void generateMethodParameters_CorrectInput(){
        String parameters = "id: int, name:String[],books:List<String>";
        ArrayList<Hashtable<String, String>> expectedParameters = new ArrayList<>();

        Hashtable<String, String> param1 = new Hashtable<>();
        param1.put("parameter_datatype", "int");
        param1.put("parameter_identifier", "id");
        expectedParameters.add(param1);

        Hashtable<String, String> param2 = new Hashtable<>();
        param2.put("parameter_datatype", "String[]");
        param2.put("parameter_identifier", "name");
        expectedParameters.add(param2);

        Hashtable<String, String> param3 = new Hashtable<>();
        param3.put("parameter_datatype", "List<String>");
        param3.put("parameter_identifier", "books");
        expectedParameters.add(param3);

        ArrayList<Hashtable<String, String>> actualParameters = codeGenerator.generateMethodParameters(parameters);

        assertEquals(expectedParameters, actualParameters);
    }

    @Test
    void generateMethodParameters_IncorrectInput(){
        String parameters = "id: , :String[],";

        ArrayList<Hashtable<String, String>> expectedParameters = new ArrayList<>();

        ArrayList<Hashtable<String, String>> actualParameters = codeGenerator.generateMethodParameters(parameters);
        System.out.println(actualParameters);
        assertEquals(expectedParameters, actualParameters, "Should extract no parameters");
    }

    @Test
    void generateClassMethods_CorrectInput(){

        String method1 = "/#setOperation(id:int, name:String, books:List<Books>):void/";
        String method2 = "_+getOperation():String_";

        ArrayList<String> testMethods = new ArrayList<>();
        testMethods.add(method1);
        testMethods.add(method2);

        ArrayList<Hashtable<String, Object>> expectedMethods = new ArrayList<>();

        // if prev test case passes, we can use this method again
        ArrayList<Hashtable<String, String>> method1Parameters =
                codeGenerator.generateMethodParameters("id:int, name:String, books:List<Books>");

        Hashtable<String, Object> expectedMethod1 = new Hashtable<>();
        expectedMethod1.put("method_access_modifier", "protected");
        expectedMethod1.put("method_static", "abstract");
        expectedMethod1.put("method_identifier", "setOperation");
        expectedMethod1.put("method_parameters", method1Parameters);
        expectedMethod1.put("method_return_type", "void");
        expectedMethods.add(expectedMethod1);

        Hashtable<String, Object> expectedMethod2 = new Hashtable<>();
        expectedMethod2.put("method_access_modifier", "public");
        expectedMethod2.put("method_static", "static");
        expectedMethod2.put("method_identifier", "getOperation");
        expectedMethod2.put("method_return_type", "String");
        expectedMethods.add(expectedMethod2);

        ArrayList<Hashtable<String, Object>> actualMethods = codeGenerator.generateClassMethods(testMethods);
        assertEquals(expectedMethods,actualMethods);
    }

    @Test
    void generateClassMethods_IncorrectInput(){

        String method1 = "#123():hello";

        ArrayList<String> testMethods = new ArrayList<>();
        testMethods.add(method1);

        ArrayList<Hashtable<String, Object>> expectedMethods = new ArrayList<>();

        ArrayList<Hashtable<String, Object>> actualMethods = codeGenerator.generateClassMethods(testMethods);
        assertEquals(expectedMethods,actualMethods, "Invalid method identifier should return no method detected");
    }

    @Test
    void generateRelationships_Inheritance(){

        ClassBox parentClass = new ClassBox();
        parentClass.setName("Parent");
        parentClass.setType("abstract");

        ClassBox childClass = new ClassBox();
        childClass.setName("Child");
        childClass.setType("simple");

        ClassDiagramRelationship inheritance = new ClassDiagramRelationship(childClass, parentClass, "inheritance");

        Hashtable<String,Object> expectedClassInfo = new Hashtable<>();
        ArrayList<Hashtable<String, String>> expectedAttributes = new ArrayList<>();
        ArrayList<Hashtable<String, Object>> expectedMethods = new ArrayList<>();
        expectedClassInfo.put("class_extends", "extends");
        expectedClassInfo.put("class_extends_inherits", "Parent");
        expectedClassInfo.put("class_attributes", expectedAttributes);
        expectedClassInfo.put("class_methods", expectedMethods);

        Hashtable<String,Object> actualClassInfo = new Hashtable<>();
        ArrayList<Hashtable<String, String>> actualAttributes = new ArrayList<>();
        ArrayList<Hashtable<String, Object>> actualMethods = new ArrayList<>();
        actualClassInfo.put("class_attributes", actualAttributes);
        actualClassInfo.put("class_methods", actualMethods);
        codeGenerator.generateRelationships(childClass, actualClassInfo,actualAttributes, actualMethods);

        assertEquals(expectedClassInfo,actualClassInfo, "Inheritance should update class hashtable");
    }

    @Test
    void generateRelationships_InheritanceInterface(){

        ClassBox parentClass = new ClassBox();
        parentClass.setName("CanBark");
        parentClass.setType("interface");

        ClassBox childClass = new ClassBox();
        childClass.setName("Dog");
        childClass.setType("simple");

        ClassDiagramRelationship inheritance = new ClassDiagramRelationship(childClass, parentClass, "inheritance");

        Hashtable<String,Object> expectedClassInfo = new Hashtable<>();
        ArrayList<Hashtable<String, String>> expectedAttributes = new ArrayList<>();
        ArrayList<Hashtable<String, Object>> expectedMethods = new ArrayList<>();
        ArrayList<String> expectedParent = new ArrayList<>();
        expectedParent.add("CanBark");

        expectedClassInfo.put("class_implements", "implements");
        expectedClassInfo.put("class_implements_inherits", expectedParent);
        expectedClassInfo.put("class_attributes", expectedAttributes);
        expectedClassInfo.put("class_methods", expectedMethods);

        Hashtable<String,Object> actualClassInfo = new Hashtable<>();
        ArrayList<Hashtable<String, String>> actualAttributes = new ArrayList<>();
        ArrayList<Hashtable<String, Object>> actualMethods = new ArrayList<>();
        actualClassInfo.put("class_attributes", actualAttributes);
        actualClassInfo.put("class_methods", actualMethods);
        codeGenerator.generateRelationships(childClass, actualClassInfo,actualAttributes, actualMethods);

        assertEquals(expectedClassInfo,actualClassInfo, "Interface Inheritance should update class hashtable and methods");
    }

    @Test
    void generateRelationships_Aggregation(){
        ClassBox childClass = new ClassBox();
        childClass.setName("Book");
        childClass.setType("simple");

        ClassBox libraryClass = new ClassBox();
        libraryClass.setName("Library");
        libraryClass.setType("simple");

        ClassDiagramRelationship aggregation = new ClassDiagramRelationship(childClass, libraryClass, "aggregation");

        ArrayList<Hashtable<String, String>> expectedClassAttributes = new ArrayList<>();
        Hashtable<String,String> attributeInfo = new Hashtable<>();
        attributeInfo.put("att_access_modifier", "private");
        attributeInfo.put("att_datatype", "List<Book>");
        attributeInfo.put("att_identifier", "books");
        expectedClassAttributes.add(attributeInfo);

        ArrayList<Hashtable<String, String>> actualClassAttributes = new ArrayList<>();

        Hashtable<String,Object> classInfo = new Hashtable<>();
        codeGenerator.generateRelationships(libraryClass, classInfo, actualClassAttributes, null);

        assertEquals(expectedClassAttributes,actualClassAttributes, "Aggregation should update attributes list");
    }

    @Test
    void generateRelationships_Composition(){
        ClassBox childClass = new ClassBox();
        childClass.setName("Book");
        childClass.setType("simple");

        ClassBox libraryClass = new ClassBox();
        libraryClass.setName("Library");
        libraryClass.setType("simple");

        ClassDiagramRelationship composition = new ClassDiagramRelationship(childClass, libraryClass, "composition");

        ArrayList<Hashtable<String, String>> expectedClassAttributes = new ArrayList<>();
        Hashtable<String,String> attributeInfo = new Hashtable<>();
        attributeInfo.put("att_access_modifier", "private");
        attributeInfo.put("att_datatype", "Book");
        attributeInfo.put("att_identifier", "book");
        expectedClassAttributes.add(attributeInfo);

        ArrayList<Hashtable<String, String>> actualClassAttributes = new ArrayList<>();

        Hashtable<String,Object> classInfo = new Hashtable<>();
        codeGenerator.generateRelationships(libraryClass, classInfo, actualClassAttributes, null);

        assertEquals(expectedClassAttributes,actualClassAttributes, "Composition should update attributes list");
    }
}