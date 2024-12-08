package TestCases;

import BusinessLayer.Components.UseCaseDiagramComponents.Actor;
import BusinessLayer.Components.UseCaseDiagramComponents.UseCase;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class ActorTest {

    private Actor actor;
    private UseCase useCase;

    @BeforeEach
    public void setUp() {
        actor = new Actor("Test Actor");
        useCase = new UseCase("Test UseCase");
    }

    @Test
    public void testToJSON() {
        actor.setBounds(50,100,50,100);
        JSONObject json = actor.toJSON();

        assertEquals("Actor", json.getString("type"));
        assertEquals("Test Actor", json.getString("name"));

        JSONObject bounds = json.getJSONObject("bounds");
        assertEquals(50, bounds.getInt("x"));
        assertEquals(100, bounds.getInt("y"));
        assertEquals(50, bounds.getInt("width"));
        assertEquals(100, bounds.getInt("height"));
    }

    @Test
    public void testAddUseCase() {
        actor.addUseCase(useCase);

        assertTrue(actor.getUseCases().contains(useCase));
        // Check bidirectional relationship
        assertTrue(useCase.getActors().contains(actor));
    }

    @Test
    public void testRemoveUseCase() {
        actor.addUseCase(useCase);
        actor.removeUseCase(useCase);

        assertFalse(actor.getUseCases().contains(useCase));
        // Check bidirectional relationship
        assertFalse(useCase.getActors().contains(actor));
    }

}
