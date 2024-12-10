/**
 * This package contains the business logic for handling deserialization of UseCase Diagrams.
 */
package BusinessLayer.Diagrams;

import BusinessLayer.Components.ClassDiagramComponents.ClassBox;
import BusinessLayer.Components.ClassDiagramComponents.ClassDiagramRelationship;
import BusinessLayer.Components.UseCaseDiagramComponents.Actor;
import BusinessLayer.Components.UseCaseDiagramComponents.UseCase;
import BusinessLayer.Components.UseCaseDiagramComponents.UseCaseDiagramRelationship;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;


/**
 * Custom deserializer for the {@link UseCaseDiagram} class.
 * including its components such as Actors, Use Cases, and Relationships.
 */
public class UseCaseDiagramDeserializer extends JsonDeserializer<UseCaseDiagram>{
        @Override
        public UseCaseDiagram deserialize(JsonParser parser, DeserializationContext context)
                throws IOException, JsonProcessingException {
            // Parse the JSON into a JSONObject
            JSONObject jsonObject = new JSONObject(parser.readValueAsTree().toString());

            UseCaseDiagram usecasediagram = new UseCaseDiagram();

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
                    if ("Actor".equals(type)) {

                        Actor actor = new Actor(componentJson.getString("name"));

                        if (componentJson.has("bounds")) {
                            JSONObject boundsJson = componentJson.getJSONObject("bounds");
                            int x = boundsJson.getInt("x");
                            int y = boundsJson.getInt("y");
                            int width = boundsJson.getInt("width");
                            int height = boundsJson.getInt("height");

                            Rectangle bounds = new Rectangle(x, y, width, height);
                            actor.setBounds(bounds); // Assuming `setBounds()` is implemented
                        }

                        usecasediagram.addComponents(actor);
                    } else if ("UseCase".equals(type)) {
                        UseCase usecase = new UseCase(componentJson.getString("name"));

                        if (componentJson.has("bounds")) {
                            JSONObject boundsJson = componentJson.getJSONObject("bounds");
                            int x = boundsJson.getInt("x");
                            int y = boundsJson.getInt("y");
                            int width = boundsJson.getInt("width");
                            int height = boundsJson.getInt("height");

                            Rectangle bounds = new Rectangle(x, y, width, height);
                            usecase.setBounds(bounds); // Assuming `setBounds()` is implemented
                        }

                        usecasediagram.addComponents(usecase);
                    } else if ("UseCaseDiagramRelationship".equals(type)) {
                        // Retrieve `from` and `to` components
                        JSONObject fromJson = componentJson.getJSONObject("from");
                        JSONObject toJson = componentJson.getJSONObject("to");

                        Actor fromActor = null;
                        UseCase fromUseCase = null;
                        Actor toActor = null;
                        UseCase toUseCase = null;

                        // Handle `from` component
                        String fromType = fromJson.optString("type");
                        if ("Actor".equals(fromType)) {
                            fromActor = new Actor(fromJson.getString("name"));
                            if (fromJson.has("bounds")) {
                                JSONObject fromBoundsJson = fromJson.getJSONObject("bounds");
                                int x = fromBoundsJson.getInt("x");
                                int y = fromBoundsJson.getInt("y");
                                int width = fromBoundsJson.getInt("width");
                                int height = fromBoundsJson.getInt("height");

                                Rectangle fromBounds = new Rectangle(x, y, width, height);
                                fromActor.setBounds(fromBounds);
                            }
                        } else if ("UseCase".equals(fromType)) {
                            fromUseCase = new UseCase(fromJson.getString("name"));
                            if (fromJson.has("bounds")) {
                                JSONObject fromBoundsJson = fromJson.getJSONObject("bounds");
                                int x = fromBoundsJson.getInt("x");
                                int y = fromBoundsJson.getInt("y");
                                int width = fromBoundsJson.getInt("width");
                                int height = fromBoundsJson.getInt("height");

                                Rectangle fromBounds = new Rectangle(x, y, width, height);
                                fromUseCase.setBounds(fromBounds);
                            }
                        } else {
                            throw new IllegalArgumentException("Invalid `from` component type for `UseCaseDiagramRelationship`.");
                        }

                        // Handle `to` component
                        String toType = toJson.optString("type");
                        if ("Actor".equals(toType)) {
                            toActor = new Actor(toJson.getString("name"));
                            if (toJson.has("bounds")) {
                                JSONObject toBoundsJson = toJson.getJSONObject("bounds");
                                int x = toBoundsJson.getInt("x");
                                int y = toBoundsJson.getInt("y");
                                int width = toBoundsJson.getInt("width");
                                int height = toBoundsJson.getInt("height");

                                Rectangle toBounds = new Rectangle(x, y, width, height);
                                toActor.setBounds(toBounds);
                            }
                        } else if ("UseCase".equals(toType)) {
                            toUseCase = new UseCase(toJson.getString("name"));
                            if (toJson.has("bounds")) {
                                JSONObject toBoundsJson = toJson.getJSONObject("bounds");
                                int x = toBoundsJson.getInt("x");
                                int y = toBoundsJson.getInt("y");
                                int width = toBoundsJson.getInt("width");
                                int height = toBoundsJson.getInt("height");

                                Rectangle toBounds = new Rectangle(x, y, width, height);
                                toUseCase.setBounds(toBounds);
                            }
                        } else {
                            throw new IllegalArgumentException("Invalid `to` component type for `UseCaseDiagramRelationship`.");
                        }

                        // Create the relationship
                        UseCaseDiagramRelationship relationship = null;

                        if (fromActor != null && toActor != null) {
                            relationship = new UseCaseDiagramRelationship(fromActor, toActor, componentJson.getString("name"));
                        } else if (fromActor != null && toUseCase != null) {
                            relationship = new UseCaseDiagramRelationship(fromActor, toUseCase, componentJson.getString("name"));
                        } else if (fromUseCase != null && toActor != null) {
                            relationship = new UseCaseDiagramRelationship(fromUseCase, toActor, componentJson.getString("name"));
                        } else if (fromUseCase != null && toUseCase != null) {
                            relationship = new UseCaseDiagramRelationship(fromUseCase, toUseCase, componentJson.getString("name"));
                        }

                        if (relationship != null) {
                            // Set name and label
                            if (componentJson.has("name")) {
                                relationship.setName(componentJson.getString("name"));
                            }

                            if (componentJson.has("label")) {
                                relationship.setLabel(componentJson.getString("label"));
                            }

                            // Set bounds for the relationship
                            if (componentJson.has("bounds")) {
                                JSONObject boundsJson = componentJson.getJSONObject("bounds");
                                int x = boundsJson.getInt("x");
                                int y = boundsJson.getInt("y");
                                int width = boundsJson.getInt("width");
                                int height = boundsJson.getInt("height");

                                Rectangle bounds = new Rectangle(x, y, width, height);
                                relationship.setBounds(bounds);
                            }
                            JSONObject pointJson = componentJson.getJSONObject("startPoint");
                            relationship.setStartPoint(new Point(pointJson.getInt("x"), pointJson.getInt("y")));

                            JSONObject endPointJson = componentJson.getJSONObject("endPoint");
                            relationship.setEndPoint(new Point(endPointJson.getInt("x"), endPointJson.getInt("y")));

                            usecasediagram.addComponents(relationship);
                        } else {
                            throw new IllegalArgumentException("Failed to create `UseCaseDiagramRelationship` due to missing components.");
                        }
                    }
                }
            }
            return usecasediagram;
        }
}
