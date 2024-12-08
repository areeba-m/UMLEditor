package TestCases;

import BusinessLayer.Components.UseCaseDiagramComponents.Actor;
import BusinessLayer.Components.UseCaseDiagramComponents.UseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.event.MouseEvent;

import static org.junit.jupiter.api.Assertions.*;

class UseCaseTest {
    private UseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new UseCase("Sample UseCase");
    }
    @Test
    void testAddActorSuccessfully() {
        Actor actor = new Actor("Sample Actor");
        useCase.addActor(actor);

        assertTrue(useCase.getActors().contains(actor));
        assertTrue(actor.getUseCases().contains(useCase));
    }

    @Test
    void testAddActorDoesNotDuplicate() {
        Actor actor = new Actor("Sample Actor");
        useCase.addActor(actor);
        useCase.addActor(actor);

        assertEquals(1, useCase.getActors().size());
    }

    @Test
    void testRemoveActorSuccessfully() {
        Actor actor = new Actor("Sample Actor");
        useCase.addActor(actor);
        useCase.removeActor(actor);

        assertFalse(useCase.getActors().contains(actor));
        assertFalse(actor.getUseCases().contains(useCase));
    }

    @Test
    void testRemoveActorNotInList() {
        Actor actor = new Actor("Non-Connected Actor");
        useCase.removeActor(actor);

        // Ensure no errors occur and actors list remains unchanged
        assertEquals(0, useCase.getActors().size());
    }

    @Test
    void testToJsonSerialization() {
        Rectangle bounds = new Rectangle(10, 20, 200, 100);
        useCase.setBounds(bounds);

        var json = useCase.toJSON();

        assertEquals("UseCase", json.getString("type"));
        assertEquals("Sample UseCase", json.getString("name"));
        assertEquals(10, json.getJSONObject("bounds").getInt("x"));
        assertEquals(20, json.getJSONObject("bounds").getInt("y"));
        assertEquals(200, json.getJSONObject("bounds").getInt("width"));
        assertEquals(100, json.getJSONObject("bounds").getInt("height"));
    }

}
