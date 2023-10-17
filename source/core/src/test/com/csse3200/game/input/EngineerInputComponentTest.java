package com.csse3200.game.input;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Camera;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EngineerInputComponentTest {
    private EngineerInputComponent engineerInputComponent;


    @BeforeEach
    public void setup() {
        EntityService mockEntityService = mock(EntityService.class);
        ServiceLocator.registerEntityService(mockEntityService);

        Camera mockCamera = mock(Camera.class);
        Game game = mock(Game.class);
        this.engineerInputComponent = new EngineerInputComponent(game, mockCamera);
    }

    @Test
    public void testTouchDownOnTileWithNoSelectedEngineer() {
        when(ServiceLocator.getEntityService().getEntityAtPositionLayer(anyFloat(), anyFloat(), anyShort())).thenReturn(null);
        // nothing happened -> false
        boolean result = engineerInputComponent.touchDown(0, 0, 0, 0);
        assertFalse(result);
        // no engineer should be selected
        assertNull(engineerInputComponent.selectedEngineer);
    }
}
