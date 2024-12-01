package BusinessLayer.Diagrams;

import BusinessLayer.Components.ClassDiagramComponents.ClassBox;
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
                // Add the ClassBox to the ClassDiagram
                classDiagram.addComponent(classBox);
            }
        }

        return classDiagram; // Return the fully constructed object
    }
}
