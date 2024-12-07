package BusinessLayer.Diagrams;

import BusinessLayer.Components.ClassDiagramComponents.ClassBox;
import BusinessLayer.Components.ClassDiagramComponents.ClassDiagramRelationship;
import BusinessLayer.Diagrams.ClassDiagram;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;

public class ClassDiagramDeserializer extends JsonDeserializer<ClassDiagram> {

    @Override
    public ClassDiagram deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        // Parse the JSON into a JSONObject
        JSONObject jsonObject = new JSONObject(parser.readValueAsTree().toString());

        // Create a ClassDiagram instance
        ClassDiagram classDiagram = new ClassDiagram();

        // Deserialize components (array case)
        if (jsonObject.has("components")) {
            JSONArray componentsArray = jsonObject.optJSONArray("components");

            // Handle if `components` is an object instead of an array
            if (componentsArray == null) {
                // Convert single component to an array for uniform handling
                componentsArray = new JSONArray().put(jsonObject.getJSONObject("components"));
            }

            // Iterate through each component in the array
            for (int i = 0; i < componentsArray.length(); i++) {
                JSONObject componentJson = componentsArray.getJSONObject(i);
                String type = componentJson.optString("type");
                if ("ClassBox".equals(type)) {
                    // Create and populate a ClassBox from the component JSON
                    ClassBox classBox = new ClassBox();
                    classBox.setMethods(componentJson.getJSONArray("methods").toList());
                    classBox.setName(componentJson.getString("name"));
                    classBox.setWidth(componentJson.getInt("width"));
                    classBox.setAttributes(componentJson.getJSONArray("attributes").toList());
                    classBox.setType(componentJson.getString("classType"));

                    // Deserialize the point object
                    JSONObject pointJson = componentJson.getJSONObject("point");
                    classBox.setPoint(new Point(pointJson.getInt("x"), pointJson.getInt("y")));
                    // Deserialize height and width
                    classBox.setHeight(componentJson.getInt("height"));

                    if (componentJson.has("bounds")) {
                        JSONObject boundsJson = componentJson.getJSONObject("bounds");
                        int x = boundsJson.getInt("x");
                        int y = boundsJson.getInt("y");
                        int width = boundsJson.getInt("width");
                        int height = boundsJson.getInt("height");

                        Rectangle bounds = new Rectangle(x, y, width, height);
                        classBox.setBounds(bounds); // Assuming `setBounds()` is implemented
                    }

                    if (componentJson.has("location")) {
                        JSONObject locationJson = componentJson.getJSONObject("location");
                        int x = locationJson.getInt("x");
                        int y = locationJson.getInt("y");
                        classBox.setLocation(new Point(x, y));
                    }
                    //classBox.updatePreferredSize();
                    // Add the ClassBox to the ClassDiagram
                    classDiagram.addComponents(classBox);
                } else if ("ClassDiagramRelationship".equals(type)) {

                    ClassBox fromBox = new ClassBox();
                    ClassBox toBox = new ClassBox();

                    if (componentJson.has("from")) {
                        JSONObject fromJson = componentJson.getJSONObject("from");
                        //ClassBox fromBox = new ClassBox();
                        fromBox.setName(fromJson.getString("name"));
                        fromBox.setType(fromJson.getString("classType"));
                        fromBox.setAttributes(fromJson.getJSONArray("attributes").toList());
                        fromBox.setMethods(fromJson.getJSONArray("methods").toList());
                        fromBox.setHeight(fromJson.getInt("height"));
                        fromBox.setWidth(fromJson.getInt("width"));
                        JSONObject fromPointJson = fromJson.getJSONObject("point");
                        fromBox.setPoint(new Point(fromPointJson.getInt("x"), fromPointJson.getInt("y")));//relationship.setFrom(fromBox);
                        // Deserialize bounds (if present)
                        if (fromJson.has("bounds")) {
                            JSONObject boundsJson = fromJson.getJSONObject("bounds");
                            int x = boundsJson.getInt("x");
                            int y = boundsJson.getInt("y");
                            int width = boundsJson.getInt("width");
                            int height = boundsJson.getInt("height");
                            fromBox.setBounds(x, y, width, height);
                        }
//                        if (componentJson.has("location")) {
//                            JSONObject locationJson = componentJson.getJSONObject("location");
//                            int x = locationJson.getInt("x");
//                            int y = locationJson.getInt("y");
//                            fromBox.setLocation(new Point(x, y));
//                        }
                    }

                    if (componentJson.has("to")) {
                        JSONObject toJson = componentJson.getJSONObject("to");
                        //ClassBox toBox = new ClassBox();
                        toBox.setName(toJson.getString("name"));
                        toBox.setType(toJson.getString("classType"));
                        toBox.setAttributes(toJson.getJSONArray("attributes").toList());
                        toBox.setMethods(toJson.getJSONArray("methods").toList());
                        toBox.setHeight(toJson.getInt("height"));
                        toBox.setWidth(toJson.getInt("width"));
                        JSONObject toPointJson = toJson.getJSONObject("point");
                        toBox.setPoint(new Point(toPointJson.getInt("x"), toPointJson.getInt("y")));
                        //relationship.setTo(toBox);
                        // Deserialize bounds (if present)
                        if (toJson.has("bounds")) {
                            JSONObject boundsJson = toJson.getJSONObject("bounds");
                            int x = boundsJson.getInt("x");
                            int y = boundsJson.getInt("y");
                            int width = boundsJson.getInt("width");
                            int height = boundsJson.getInt("height");
                            toBox.setBounds(x, y, width, height);
                        }
//                        if (toJson.has("location")) {
//                            JSONObject locationJson = toJson.getJSONObject("location");
//                            toBox.setLocation(locationJson.getInt("x"), locationJson.getInt("y"));
//                        }
                    }

                    ClassDiagramRelationship relationship = new ClassDiagramRelationship(fromBox, toBox, componentJson.getString("name"));

                    relationship.setName(componentJson.getString("name"));

                    JSONObject pointJson = componentJson.getJSONObject("point");
                    relationship.setPoint(new Point(pointJson.getInt("x"), pointJson.getInt("y")));

                    JSONObject endPointJson = componentJson.getJSONObject("endPoint");
                    relationship.setEndPoint(new Point(endPointJson.getInt("x"), endPointJson.getInt("y")));

                    // Add bounds for the relationship itself (if applicable)
                    if (componentJson.has("bounds")) {
                        JSONObject boundsJson = componentJson.getJSONObject("bounds");
                        int x = boundsJson.getInt("x");
                        int y = boundsJson.getInt("y");
                        int width = boundsJson.getInt("width");
                        int height = boundsJson.getInt("height");
                        relationship.setBounds(new Rectangle(x, y, width, height)); // Assuming `setBounds` exists
                    }

                    if (componentJson.has("location")) {
                        JSONObject locationJson = componentJson.getJSONObject("location");
                        int x = locationJson.getInt("x");
                        int y = locationJson.getInt("y");
                        relationship.setLocation(new Point(x, y));
                    }

//                    if (componentJson.has("from")) {
//                        relationship.setFrom(fromBox);
//                    }
//                    if (componentJson.has("to")) {
//                        relationship.setTo(toBox);
//                    }
                    classDiagram.addComponents(relationship);
                } else {
                    throw new IllegalArgumentException("Unknown component type: " + type);
                }
            }
        }
        return classDiagram;
    }
}

