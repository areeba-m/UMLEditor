package Data;

import BusinessLayer.Components.ClassDiagramComponents.ClassBox;
import BusinessLayer.Components.ClassDiagramComponents.ClassDiagramRelationship;
import BusinessLayer.Components.UMLComponent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

public class CodeGenerator {

    ArrayList<UMLComponent> components;
    String regex = "[^a-zA-Z0-9\\[\\]<>]";
    String replaceDigitsRegex = "^[0-9]+";

    public CodeGenerator() {
        // used for testcases
    }

    public CodeGenerator(ArrayList<UMLComponent> components) {
        this.components = components;
    }

    public void generateCode(File directory) throws IOException {

        for (UMLComponent component : components) {

            Hashtable<String, Object> classInfo = new Hashtable<>();

            if (component instanceof ClassBox) {

                // general class characteristics
                classInfo.put("class_access_modifier", "public"); // public class by default
                if (component.getType().equalsIgnoreCase("simple")) {
                    classInfo.put("class_type", "");
                } else {
                    classInfo.put("class_type", component.getType().toLowerCase()); // abstract, interface
                }
                classInfo.put("class_name", component.getName().replaceFirst(replaceDigitsRegex, ""));

                // extract attributes of this class
                ArrayList<String> originalAttributes = ((ClassBox) component).getAttributes();
                ArrayList<Hashtable<String, String>> classAttributes = generateClassAttributes(originalAttributes);
                classInfo.put("class_attributes", classAttributes);

                // extract methods of this class
                ArrayList<String> originalMethods = ((ClassBox) component).getMethods();
                ArrayList<Hashtable<String, Object>> classMethods = generateClassMethods(originalMethods);
                classInfo.put("class_methods", classMethods);

                // extract relationships of this class
                generateRelationships((ClassBox)component, classInfo, classAttributes, classMethods);

                // write to file
                writeToFile(directory, classInfo);
            }
        }
    }

    public ArrayList<Hashtable<String, String>> generateClassAttributes(ArrayList<String> originalAttributes) {
        ArrayList<Hashtable<String, String>> classAttributes = new ArrayList<>();

        for (String attribute : originalAttributes) {
            System.out.println("Given attribute: " + attribute);
            Hashtable<String, String> attributeInfo = new Hashtable<>();

            if (attribute.contains("#")) {
                attributeInfo.put("att_access_modifier", "protected");
            } else if (attribute.contains("+")) {
                attributeInfo.put("att_access_modifier", "public");
            } else {
                attributeInfo.put("att_access_modifier", "private");
            }

            if (attribute.trim().startsWith("_") && attribute.trim().endsWith("_")) {
                attributeInfo.put("att_static", "static");
            } else {
                attributeInfo.put("att_static", "");
            }

            // _-id:String_
            int colonPosition = attribute.indexOf(":");
            try {
                String identifier = attribute.substring(0, colonPosition);
                identifier = identifier.replaceAll(regex, "").trim().
                        replaceFirst(replaceDigitsRegex, "");
                //System.out.println("identifier: " + identifier);

                String datatype = attribute.substring(colonPosition + 1);
                datatype = datatype.replaceAll(regex, "").trim().
                        replaceFirst(replaceDigitsRegex, "");
                //System.out.println("datatype: " + datatype);

                if (!(datatype.isEmpty() || identifier.isEmpty())) {
                    attributeInfo.put("att_datatype", datatype);
                    attributeInfo.put("att_identifier", identifier);

                } else {
                    continue; // case: no datatype after :
                }
            } catch (IndexOutOfBoundsException ex) {
                continue; // case: colonPosition out of bounds
            }


            classAttributes.add(attributeInfo);
        }

        return classAttributes;
    }

    public ArrayList<Hashtable<String, Object>> generateClassMethods(ArrayList<String> originalMethods) {

        ArrayList<Hashtable<String, Object>> classMethods = new ArrayList<>();

        for(String method: originalMethods){
            System.out.println("Given method: " + method);

            Hashtable<String, Object> methodInfo = getOneMethod(method);

            if(methodInfo != null){
                classMethods.add(methodInfo);
            }
        }

        return classMethods;
    }

    public ArrayList<Hashtable<String, String>> generateMethodParameters(String parameters){

        ArrayList<Hashtable<String, String>> methodParameters = new ArrayList<>();
        String[] args = parameters.split(",");

        for(String arg: args){
            Hashtable<String, String> parameterInfo = new Hashtable<>();

            int colonPosition = arg.indexOf(":");
            try {
                String parameterIdentifier = arg.substring(0, colonPosition);
                parameterIdentifier = parameterIdentifier.replaceAll(regex, "").trim().
                        replaceFirst(replaceDigitsRegex, "");
                //System.out.println("parameter identifier: " + parameterIdentifier);

                String parameterDatatype = arg.substring(colonPosition + 1);
                parameterDatatype = parameterDatatype.replaceAll(regex, "").trim().
                        replaceFirst(replaceDigitsRegex, "");
                //System.out.println("parameter datatype: " + parameterDatatype);

                if (!(parameterDatatype.isEmpty() || parameterIdentifier.isEmpty())) {
                    parameterInfo.put("parameter_datatype", parameterDatatype);
                    parameterInfo.put("parameter_identifier", parameterIdentifier);

                } else {
                    continue; // case: no datatype after :
                }
            } catch (IndexOutOfBoundsException ex) {
                continue; // skip parameter
            }

            methodParameters.add(parameterInfo);
        }

        return methodParameters;
    }


    public Hashtable<String,Object> getOneMethod(String method){

        Hashtable<String, Object> methodInfo = new Hashtable<>();
        // /+setOperation(id:int, name:String):void/
        // ---> public abstract void setOperation(int i, String name)
        // _+getOperation():String_

        if (method.contains("#")) {
            methodInfo.put("method_access_modifier", "protected");
        } else if (method.contains("-")) {
            methodInfo.put("method_access_modifier", "private");
        } else {
            methodInfo.put("method_access_modifier", "public");
        }

        if (method.trim().startsWith("@")){
            // user will put @ for override method
            methodInfo.put("method_override", "@Override");
            method = method.replaceFirst("@", "");
        }

        if (method.trim().startsWith("_") && method.trim().endsWith("_")) {
            methodInfo.put("method_static", "static");
        } else if (method.trim().startsWith("/") && method.trim().endsWith("/")){
            methodInfo.put("method_static", "abstract");
            if(methodInfo.get("method_access_modifier").equals("private")) {
                methodInfo.put("method_access_modifier", "protected"); // an abstract method can not be private
            }

        } else {
            methodInfo.put("method_static", "");
        }

        int openBracketPosition = method.indexOf("(");
        int closeBracketPosition = method.indexOf(")");

        try{
            String methodIdentifier = method.substring(0, openBracketPosition);
            methodIdentifier = methodIdentifier.replaceAll(regex, "").trim().
                    replaceFirst(replaceDigitsRegex, "");
            if(!methodIdentifier.isEmpty())
                methodInfo.put("method_identifier", methodIdentifier);
            else
                return null; // skip if invalid identifier

            String parameters = method.substring(openBracketPosition+1, closeBracketPosition);
            if(!parameters.trim().isEmpty()){
                ArrayList<Hashtable<String, String>> methodParameters = generateMethodParameters(parameters);
                methodInfo.put("method_parameters", methodParameters); // this key will only exist if parameters exist
            }

            String returnType = (closeBracketPosition + 1 < method.length())
                    ? method.substring(closeBracketPosition + 1).
                    replaceAll(regex, "").trim().
                    replaceFirst(replaceDigitsRegex, "") : "";
            methodInfo.put("method_return_type", returnType.isEmpty() ? "void" : returnType);

        } catch (IndexOutOfBoundsException ex){
            return null; // skip this method
        }

        return methodInfo;
    }

    public void generateRelationships(ClassBox component, Hashtable<String, Object> classInfo,
                                      ArrayList<Hashtable<String, String>> classAttributes,
                                      ArrayList<Hashtable<String, Object>> classMethods) {

        ArrayList<ClassDiagramRelationship> originalRelationships = component.getRelationships();
        for(ClassDiagramRelationship relationship: originalRelationships){

            if(relationship.getName().equalsIgnoreCase("inheritance")){
                if(relationship.getFrom().equals(component)){
                    // current component is child

                    String parentClass = relationship.getTo().getName();
                    if(relationship.getTo().getType().equalsIgnoreCase("interface")){

                        ArrayList<String> implementsInfo;
                        if(classInfo.containsKey("class_implements_inherits")){
                            implementsInfo = (ArrayList<String>) classInfo.get("class_implements_inherits");
                        } else {
                            implementsInfo = new ArrayList<>();
                        }
                        implementsInfo.add(parentClass); // list of implemented classes
                        classInfo.put("class_implements", "implements");
                        classInfo.put("class_implements_inherits", implementsInfo);

                        ArrayList<String> parentMethods = ((ClassBox) relationship.getTo()).getMethods();
                        for(String method: parentMethods){
                            Hashtable<String,Object> parentMethodInfo = getOneMethod(method);
                            if(parentMethodInfo != null) {
                                classMethods.add(parentMethodInfo); // add all of parents method in child for interface
                            }
                        }

                    } else { // abstract, normal

                        classInfo.put("class_extends", "extends");
                        classInfo.put("class_extends_inherits", parentClass); // key for extend parent class

                        ArrayList<String> parentMethods = ((ClassBox) relationship.getTo()).getMethods();
                        for(String method: parentMethods){
                            Hashtable<String,Object> parentMethodInfo = getOneMethod(method);
                            if(parentMethodInfo != null) {
                                String isStatic = (String) parentMethodInfo.get("method_static");

                                if (isStatic != null && isStatic.equals("abstract")) {
                                    classInfo.put("class_static", "abstract"); // abstract method must be in abstract class

                                    parentMethodInfo.put("method_static", "");
                                    parentMethodInfo.put("method_override", "@Override");

                                    classMethods.add(parentMethodInfo);
                                }
                            }
                        }
                    }
                }
                // do nothing if current class is a parent
            } else if(relationship.getName().equalsIgnoreCase("aggregation")){

                if(relationship.getTo().equals(component)) {
                    // current class contains list of 'from'
                    String datatype = "List<".concat(relationship.getFrom().getName()).concat(">");
                    String identifier = relationship.getFrom().getName().toLowerCase().concat("s");

                    Hashtable<String,String> attributeInfo = new Hashtable<>();
                    attributeInfo.put("att_access_modifier", "private");
                    attributeInfo.put("att_datatype", datatype);
                    attributeInfo.put("att_identifier", identifier);
                    classAttributes.add(attributeInfo);

                    classInfo.put("class_attributes", classAttributes); // update attributes

                }
            } else if (relationship.getName().equalsIgnoreCase("composition")){

                if(relationship.getTo().equals(component)) {
                    // current class contains object of 'from'
                    String datatype = relationship.getFrom().getName();
                    String identifier = relationship.getFrom().getName().toLowerCase();

                    Hashtable<String,String> attributeInfo = new Hashtable<>();
                    attributeInfo.put("att_access_modifier", "private");
                    attributeInfo.put("att_datatype", datatype);
                    attributeInfo.put("att_identifier", identifier);
                    classAttributes.add(attributeInfo);

                    classInfo.put("class_attributes", classAttributes); // update attributes

                }
            }
            // else do nothing for association
        }
    }

    private void writeToFile(File outputDir, Hashtable<String,Object> classInfo) throws IOException {
        String className = (String)classInfo.get("class_name");
        String classType = (String)classInfo.get("class_type"); // abstract, interface

        StringBuilder code = new StringBuilder();

        code.append("public ").append(classType).append(" class ").append(className);

        if(classInfo.containsKey("class_extends")){
            String parentName = (String)classInfo.get("class_extends_inherits");
            code.append(" extends ").append(parentName);
        }
        if(classInfo.containsKey("class_implements")){
            ArrayList<String> implementsParentName = (ArrayList<String>) classInfo.get("class_implements_inherits");
            int noOfParents = implementsParentName.size();
            int count = 0;
            code.append(" implements ");
            for(String parentName: implementsParentName){
                code.append(parentName);
                count++;
                if(count != noOfParents){
                    code.append(", ");
                }
            }
        }
        code.append(" {\n\n");

        ArrayList<Hashtable<String,String>> attributes = (ArrayList<Hashtable<String,String>>) classInfo.get("class_attributes");
        for(Hashtable<String,String> attributeInfo : attributes){
            String access = attributeInfo.get("att_access_modifier");
            String isStatic = attributeInfo.get("att_static");
            String datatype = attributeInfo.get("att_datatype");
            String identifier = attributeInfo.get("att_identifier");

            code.append("\t").append(access).append(" ");
            if(isStatic != null && !isStatic.isEmpty()){
                code.append(isStatic).append(" ");
            }
            code.append(datatype).append(" ").append(identifier).append(";\n");
        }

        code.append("\n");

        ArrayList<Hashtable<String, Object>> methods = (ArrayList<Hashtable<String, Object>>) classInfo.get("class_methods");
        for(Hashtable<String, Object> methodInfo: methods){
            if(methodInfo.containsKey("method_override")){
                code.append("\t@Override\n");
            }
            String access = (String) methodInfo.get("method_access_modifier");
            String isStatic = (String) methodInfo.get("method_static");
            String returnType = (String) methodInfo.get("method_return_type");
            String identifier = (String) methodInfo.get("method_identifier");

            code.append("\t").append(access).append(" ");
            if(isStatic != null && !isStatic.isEmpty()){
                code.append(isStatic).append(" ");
            }
            code.append(returnType).append(" ").append(identifier).append("(");

            if(methodInfo.containsKey("method_parameters")) {
                ArrayList<Hashtable<String, String>> parameters = (ArrayList<Hashtable<String, String>>) methodInfo.get("method_parameters");
                int noOfParams = parameters.size();
                int count = 0;
                for (Hashtable<String, String> parameterInfo : parameters) {
                    String paramDatatype = parameterInfo.get("parameter_datatype");
                    String paramIdentifier = parameterInfo.get("parameter_identifier");
                    code.append(paramDatatype).append(" ").append(paramIdentifier);
                    count++;
                    if (count != noOfParams) {
                        code.append(", ");
                    }
                }
            }
            code.append(")");
            if(isStatic != null && isStatic.equals("abstract")){
                code.append(";\n\n");

            } else {
                code.append(" {\n");
                code.append("\t\t// TODO: Implement this method!\n");

                if(!returnType.equals("void")){
                    code.append("\t\treturn null;\n");
                }
                code.append("\t}\n\n");
            }

        }
        code.append("}");

        File outputFile = new File(outputDir, className + ".java");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            writer.write(code.toString());
        }

    }

}
